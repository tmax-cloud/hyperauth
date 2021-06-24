package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.jpa.EmailVerification;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
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

@Slf4j
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
    public Response post(@PathParam("email") final String username, @QueryParam("resetPassword") String resetPassword) throws Throwable {
        log.info("***** POST /email");
        log.info("username : " + username);

        String email = null;
        // Validate If User Exists with username
        if (resetPassword != null && resetPassword.equalsIgnoreCase("t")){
            if (session.users().getUserByUsername(username, session.getContext().getRealm()) != null){
                UserModel user = session.users().getUserByUsername(username, session.getContext().getRealm());
                RealmModel realm = session.getContext().getRealm();
                email = user.getEmail();

                // 비밀번호가 없는 SNS 회원가입 유저의 경우, 에러발생
                if( !(session.userCredentialManager().getStoredCredentialsByType(realm, user, "password")!= null
                        && session.userCredentialManager().getStoredCredentialsByType(realm, user, "password").size() >0
                        && session.userCredentialManager().getStoredCredentialsByType(realm, user, "password").get(0) != null) ){
                    status = Status.BAD_REQUEST;
                    out = "Federated Identity User";
                    return Util.setCors(status, out);
                }
            }else{
                status = Status.BAD_REQUEST;
                out = "No Corresponding User";
                return Util.setCors(status, out);
            }
        } else {
            if (session.users().getUserByUsername(username, session.getContext().getRealm()) != null) {
                status = Status.BAD_REQUEST;
                out = "User Already Exists with the Email";
                return Util.setCors(status, out);
            }
        }

        String code = Util.numberGen(6, 1);
        log.debug("code : " + code);

        //Create New Entity
        EmailVerification entity = new EmailVerification();
        entity.setId(KeycloakModelUtils.generateId());
        entity.setEmail(username); //DB에는 username을 저장하는 것으로 한다. 이메일이 중복될수있는 가능성이 생김.(ex 충남대) 210614 by taegeon_woo
        entity.setCode(code);
        entity.setVerificationDate( new Timestamp(time));

        try {
            getEntityManager().persist(entity);
            log.info("DB insert Success");
        } catch(Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "DB Insert Failed";
            return Util.setCors(status, out);
        }
        String subject = "[Tmax 통합계정] 비밀번호를 재설정 해주세요.";
        String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/forgot-password-verification-code.html").replaceAll("%%VERIFY_CODE%%", code);

        String emailTheme = session.realms().getRealmByName(session.getContext().getRealm().getName()).getEmailTheme();
        if(!emailTheme.equalsIgnoreCase("tmax") && !emailTheme.equalsIgnoreCase("base") && !emailTheme.equalsIgnoreCase("keycloak")) {
            subject = "[" + emailTheme + "] 비밀번호를 재설정 해주세요.";
            body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/" + emailTheme + "/email/html/etc/forgot-password-verification-code.html").replaceAll("%%VERIFY_CODE%%", code);
        }

        try {
            Util.sendMail(session, email, subject, body, null, session.getContext().getRealm().getId());
            status = Status.OK;
            out = "Email Send Success";
            return Util.setCors(status, out);

        } catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "Email Send Failed";
            return Util.setCors(status, out);
        }
    }

	@GET
    @Path("{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("email") final String username, @QueryParam("code") String code, @QueryParam("resetPassword") String resetPassword) {
        log.info("***** GET /email");
        log.info("username : " + username);
        log.info("code : " + code);
        String codeDB = "";
        Timestamp insertTime = null;

        try {
            List< EmailVerification > emailCodeList = getEntityManager().createNamedQuery("findByEmail", EmailVerification.class)
                    .setParameter("email", username).getResultList();  //DB에는 username을 저장했다. 이메일이 중복될수있는 가능성이 생김.(ex 충남대) 210614 by taegeon_woo
            if (emailCodeList != null && emailCodeList.size() != 0) {
                codeDB = emailCodeList.get(0).getCode();
                insertTime = new Timestamp(emailCodeList.get(0).getVerificationDate().getTime());
                log.info("codeDB : " + codeDB);
                log.info("insertTime : " + insertTime);
                log.info("currentTime : " + new Timestamp(time));
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
                                .setParameter("email", username).setParameter("code", codeDB).executeUpdate();
                        log.info("Update DB IsVerified True Success");
                    }else{
                        for ( EmailVerification emailCode : emailCodeList ){
                            getEntityManager().remove(emailCode);
                        }
                        log.info("delete DB data Success");
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
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
        	out = "email Validation Failed";
            return Util.setCors(status, out);
        }
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        log.info("***** OPTIONS /email");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }
}