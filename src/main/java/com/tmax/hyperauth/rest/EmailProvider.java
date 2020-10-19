package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.caller.Constants;
import com.tmax.hyperauth.jpa.Agreement;
import com.tmax.hyperauth.jpa.EmailVerification;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.email.DefaultEmailSenderProvider;
import org.keycloak.email.EmailException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.*;
import java.util.List;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class EmailProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public EmailProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }

    Connection conn = null;
    long time = System.currentTimeMillis();
    Status status = null;
	String out = null;

    @POST
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam("email") final String email, @QueryParam("resetPassword") String resetPassword) throws Throwable {
        System.out.println("***** POST /email");
        System.out.println("email : " + email);

        // Validate If User Exists with Email
        String userName = null;
        if (resetPassword != null && resetPassword.equalsIgnoreCase("t")){
            if (session.users().getUserByEmail(email, session.realms().getRealmByName("tmax")) != null){
                userName = session.users().getUserByEmail(email, session.realms().getRealmByName("tmax")).getUsername();
                System.out.println("userName : " + userName);
            }else{
                status = Status.BAD_REQUEST;
                out = "No Corresponding User";
                return Util.setCors(status, out);
            }
        } else {
            if (session.users().getUserByEmail(email, session.realms().getRealmByName("tmax")) != null) {
                status = Status.BAD_REQUEST;
                out = "User Already Exists with the Email";
                return Util.setCors(status, out);
            }
        }

        String code = Util.numberGen(6, 1);
        System.out.println("code : " + code);

        //Create New Entity
        EmailVerification entity = new EmailVerification();
        entity.setId(KeycloakModelUtils.generateId());
        entity.setEmail(email);
        entity.setCode(code);
        entity.setVerificationDate( new Timestamp(time));

        try {
            getEntityManager().persist(entity);
            System.out.println("DB insert Success");
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
            status = Status.BAD_REQUEST;
            out = "DB Insert Failed";
            return Util.setCors(status, out);
        }

        String subject = "인증번호 입력";
        String msg = "";
        if (resetPassword != null && resetPassword.equalsIgnoreCase("t")){
            msg = "[Web발신]\n[인증번호:" + code + "] - HyperAuth\n(타인노출금지)";
        } else {
            msg = Constants.REGISTER_MAIL_BODY.replaceAll("%%verifyNumber%%", code);
        }

        try {
            Util.sendMail(email, subject, msg, null, null);
            status = Status.OK;
            out = "Email Send Success";
            return Util.setCors(status, out);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
            status = Status.BAD_REQUEST;
            out = "Email Send Failed";
            return Util.setCors(status, out);
        }
    }

	@GET
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("email") final String email, @QueryParam("code") String code, @QueryParam("resetPassword") String resetPassword) {
        System.out.println("***** GET /email");
        System.out.println("email : " + email);
    	System.out.println("code : " + code);
        String codeDB = "";
        Timestamp insertTime = null;

        try {
            List< EmailVerification > emailCodeList = getEntityManager().createNamedQuery("findByEmail", EmailVerification.class)
                    .setParameter("email", email).getResultList();
            if (emailCodeList != null && emailCodeList.size() != 0) {
                codeDB = emailCodeList.get(0).getCode();
                insertTime = new Timestamp(emailCodeList.get(0).getVerificationDate().getTime());
                System.out.println("codeDB : " + codeDB);
                System.out.println("insertTime : " + insertTime);
                System.out.println("currentTime : " + new Timestamp(time));
            } else {
                status = Status.BAD_REQUEST;
                out = "No Corresponding Request";
                return Util.setCors(status, out);
            }

            Timestamp currentTimeMinus5min = new Timestamp(time-5*1000*60);

            if ( currentTimeMinus5min.before(insertTime)){
                if (code.equalsIgnoreCase(codeDB)){
                    if (resetPassword != null && resetPassword.equalsIgnoreCase("t")){
                        getEntityManager().createNamedQuery("updateIsVerified").setParameter("isVerified", true)
                                .setParameter("email", email).setParameter("code", codeDB).executeUpdate();
                        System.out.println("Update DB IsVerified True Success");
                    }else{
                        for ( EmailVerification emailCode : emailCodeList ){
                            getEntityManager().remove(emailCode);
                        }
                        System.out.println("delete DB data Success");
                    }
                    status = Status.OK;
                    out = "email Validation Passed";

                } else {
                    status = Status.UNAUTHORIZED;
                    out = "Wrong Verification Code";
                }
            }else {
                status = Status.UNAUTHORIZED;
                out = "email Verification Time Expired";
            }
            return Util.setCors(status, out);

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
        	status = Status.BAD_REQUEST;
        	out = "email Validation Failed";
            return Util.setCors(status, out);
        }
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /email");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }
}