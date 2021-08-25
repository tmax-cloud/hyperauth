package com.tmax.hyperauth.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.*;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class PictureProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public PictureProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }

    Status status = null;
	String out = null;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userName}")
    public Response get( @PathParam("userName") final String userName) {
        log.info("***** GET /picture");
        try {
            log.info("userName : " + userName);
            String fileDir = "../welcome-content/profile-picture/" + userName;
            File dir = new File(fileDir);
            if (dir.exists() && dir.listFiles() != null && dir.listFiles().length > 0) {
                File files[] = dir.listFiles();
                String imagePath = "";
                for (int i = 0; i < files.length; i++) {
                    System.out.println(files[i] + "exists");
                    imagePath = "http://localhost:8080/profile-picture/" + userName + "/" +  files[i].getName();
                }
                out = "{ \"imagePath\" : \"" + imagePath + "\"}";
                status = Status.OK;
                return Util.setCors(status, out);
            } else {
                status = Status.OK;
                out = "{ \"reponse\" : \"No picture Exists\" }";
                return Util.setCors(status, out);
            }
        }catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "{ \"reponse\" : \"Get picture Failed\" }";
            return Util.setCors(status, out);
        }

//        try {
//            log.info ("userName : " + userName);
//            PictureModel pictureModel = new PictureModel();
//            pictureModel.setUserName(userName);
//
//            List<ProfilePicture> profileList = getEntityManager().createNamedQuery("findByUserID", ProfilePicture.class)
//                    .setParameter("userName",pictureModel.getUserName()).setParameter("realmId",session.getContext().getRealm().getId()).getResultList();
//
//            if (profileList != null && profileList.size() != 0) {
//                pictureModel.setBase64EncodeImage(new String(profileList.get(0).getImage()));
//                status = Status.OK;
//                return Util.setCors(status, pictureModel);
//            } else {
//                status = Status.OK;
//                out = "{ \"reponse\" : \"No picture Exists\" }";
//                return Util.setCors(status, out);
//            }
//        }catch (Exception e) {
//            log.error("Error Occurs!!", e);
//            status = Status.BAD_REQUEST;
//            out = "{ \"reponse\" : \"Get picture Failed\" }";
//            return Util.setCors(status, out);
//        }
    }

    @POST
    @Path("{userName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam("userName") final String userName, MultipartFormDataInput input) throws IOException {
        log.info("***** POST /picture");
        log.info ("userName : " + userName );
        try {
            if (input.getFormDataMap()!= null && input.getFormDataMap().get("imageFile") != null){
                InputPart inputPart = input.getFormDataMap().get("imageFile").get(0);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                String imageName = input.getFormDataPart("imageName", String.class, null);
                String fileName = "../welcome-content/profile-picture/" + userName + "/" + userName + "." + FilenameUtils.getExtension(imageName);
                File file = new File(fileName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } else {
                    file.delete();
                }
                log.info (file.getAbsolutePath() );

                FileOutputStream fop = new FileOutputStream(file);
                byte[] content = getBytesFromInputStream(inputStream);
                fop.write(content);
                fop.flush();
                fop.close();
            }


//        log.info ( "picture in base64 encode : " + pictureModel.getBase64EncodeImage());
//        log.info ( "pictureModel.getUserName() : " + pictureModel.getUserName());
//        log.info ( "session.getContext().getRealm().getId() : " + session.getContext().getRealm().getId());
//
//        //Delete If Already Exists
//        List<ProfilePicture> prevProfileList = getEntityManager().createNamedQuery("findByUserID", ProfilePicture.class)
//                .setParameter("userName",pictureModel.getUserName()).setParameter("realmId",session.getContext().getRealm().getId()).getResultList();
//
//        if(prevProfileList != null && prevProfileList.size() != 0) {
//            log.info("update previous one");
//            getEntityManager().createQuery("update ProfilePicture set image = '" + pictureModel.getBase64EncodeImage() + "' where userName = '" + pictureModel.getUserName() + "' and realmId = '" + session.getContext().getRealm().getId() + "'" ).executeUpdate();
////            getEntityManager().remove(prevProfileList.get(0));
//        }else{
//            log.info("create new one");
//            String id =  KeycloakModelUtils.generateId();
//
//            //Create New Entity
//            ProfilePicture entity = new ProfilePicture();
//
//            entity.setId(id);
//            entity.setUserName(pictureModel.getUserName());
//            entity.setRealmId(session.getContext().getRealm().getId());
//            entity.setImage(pictureModel.getBase64EncodeImage().getBytes());
//            getEntityManager().persist(entity);
//        }


            status = Status.OK;
            out = "{ \"reponse\" : \"profile-picture save success\" }";
        } catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "{ \"reponse\" : \"profile-picture save failed\" }";
        }
        return Util.setCors(status, out);
    }


    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userName}")
    public Response delete( @PathParam("userName") final String userName) {
        log.info("***** DELETE /picture");
        try {
            log.info("userName : " + userName);
            String fileDir = "../welcome-content/profile-picture/" + userName;
            File dir = new File(fileDir);
            if (dir.exists() && dir.listFiles() != null && dir.listFiles().length > 0) {
                File files[] = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                    System.out.println(files[i] + "deleted");
                }
                out = "{ \"reponse\" : \"Picture Delete Success\" }";
                status = Status.OK;
                return Util.setCors(status, out);
            } else {
                status = Status.OK;
                out = "{ \"reponse\" : \"No picture Exists\" }";
                return Util.setCors(status, out);
            }
        } catch (Exception e) {
                log.error("Error Occurs!!", e);
                status = Status.BAD_REQUEST;
                out = "{ \"reponse\" : \"Delete picture Failed\" }";
                return Util.setCors(status, out);
        }

//        try {
//            log.info("userName : " + userName);
//            PictureModel pictureModel = new PictureModel();
//            pictureModel.setUserName(userName);
//
//            List<ProfilePicture> profileList = getEntityManager().createNamedQuery("findByUserID", ProfilePicture.class)
//                    .setParameter("userName",pictureModel.getUserName()).setParameter("realmId",session.getContext().getRealm().getId()).getResultList();
//
//            if (profileList != null && profileList.size() != 0) {
//                log.info("delete previous one");
//                getEntityManager().remove(profileList.get(0));
//                out = "{ \"reponse\" : \"Picture Delete Success\" }";
//                status = Status.OK;
//                return Util.setCors(status, out);
//            } else {
//                status = Status.OK;
//                out = "{ \"reponse\" : \"No picture Exists\" }";
//                return Util.setCors(status, out);
//            }
//        }catch (Exception e) {
//            log.error("Error Occurs!!", e);
//            status = Status.BAD_REQUEST;
//            out = "{ \"reponse\" : \"Delete picture Failed\" }";
//            return Util.setCors(status, out);
//        }


    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        log.info("***** OPTIONS /picture");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
}