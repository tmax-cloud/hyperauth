package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.caller.Constants;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author taegeon_woo@tmax.co.kr
 */

public class TestProvider implements RealmResourceProvider {

    public static final String STATE_CHECKER_ATTRIBUTE = "state_checker";
    public static final String STATE_CHECKER_PARAMETER = "stateChecker";

    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public TestProvider(KeycloakSession session) {
        this.session = session;
        this.auth = resolveAuthentication(session);
    }

    private AuthenticationManager.AuthResult resolveAuthentication(KeycloakSession keycloakSession) {
        AppAuthManager appAuthManager = new AppAuthManager();
        RealmModel realm = keycloakSession.getContext().getRealm();

        AuthenticationManager.AuthResult authResult = appAuthManager.authenticateIdentityCookie(keycloakSession, realm);
        if (authResult != null) {
            return authResult;
        }

        return null;
    }

    private final AuthenticationManager.AuthResult auth;

    @Override
    public Object getResource() {
        return this;
    }

    Status status = null;
	String out = null;

    protected Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private boolean isValidStateChecker(MultipartFormDataInput input) {

        try {
            String actualStateChecker = input.getFormDataPart(STATE_CHECKER_PARAMETER, String.class, null);
            String requiredStateChecker = (String) session.getAttribute(STATE_CHECKER_ATTRIBUTE);

            return Objects.equals(requiredStateChecker, actualStateChecker);
        } catch (Exception ex) {
            return false;
        }
    }

    @POST
    @NoCache
    @Path("{userName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam("userName") final String userName, MultipartFormDataInput input) {
        System.out.println("***** post /test");
        System.out.println("userName : " + userName);
        if (auth == null) {
            return badRequest();
        }

        if (!isValidStateChecker(input)) {
            return badRequest();
        }

        RealmModel realm = session.getContext().getRealm();
        clientConnection = session.getContext().getConnection();
        EventBuilder event = new EventBuilder(realm, session, clientConnection); // FIXME

        String realmName = realm.getDisplayName();
        if (realmName == null) {
            realmName = session.getContext().getRealm().getName();
        }
        UserModel userModel = session.users().getUserByUsername(userName, session.realms().getRealmByName(realmName));
        if (userModel == null) {
            out = "User not found";
            status = Status.BAD_REQUEST;
            return Util.setCors(status, out);
        }

        try {
            // 유저 탈퇴 신청 API
            // Withdrawal Qualification Validation
            boolean isQualified = true;
            String unQualifiedReason = null;
            if(userModel.getAttributes()!=null) {
                for (String key : userModel.getAttributes().keySet()) {
                    if ( key.startsWith( "withdrawal_unqualified_") && userModel.getAttribute(key).get(0).equalsIgnoreCase("t")){
                        isQualified = false;
                        unQualifiedReason = key.substring(23);
                        break;
                    }
                }
            }
            if (isQualified){
                //Deletion Date Calculate
                Date currentDate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, 30);
                Date deletionDate = cal.getTime();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                String deletionDateString = transFormat.format(deletionDate);

                if(userModel.getAttributes()!=null) userModel.removeAttribute(AuthenticatorConstants.USER_ATTR_DELETION_DATE);
                userModel.setAttribute(AuthenticatorConstants.USER_ATTR_DELETION_DATE, Arrays.asList(deletionDateString));
//                        userModel.setEnabled(false);  //유저 탈퇴 철회 시나리오로 인해서 삭제
//                String email = userModel.getEmail();
//                String subject = "[Tmax 통합계정] 고객님의 계정 탈퇴 신청이 완료되었습니다.";
//                String body = Constants.ACCOUNT_WITHDRAWAL_REQUEST_BODY;
//
////                        String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/account-withdrawal-request.html"); // TODO
////                        List<Util.MailImage> imageParts = new ArrayList<>(
////                                Arrays.asList(
////                                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/logo_tmax.svg","logo_tmax.svg" ),
////                                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/secession_success.svg","secession_success.svg" ),
////                                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/bg.svg","bg.svg" )
////                                )
////                        );
//
////                        //FIXME : TESTCODE
////                        String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/account-withdrawal-completed.html");
////                        List<Util.MailImage> imageParts = new ArrayList<>(
////                                Arrays.asList(
////                                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/logo_tmax.png","logo_tmax" ),
////                                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/secession_success.png","secession_success" ),
////                                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/bg.png","bg" )
////                                )
////                        );
//
//                Util.sendMail(session, email, subject, body, null );
                status = Status.OK;
                out = " User [" + userName + "] WithDrawal Request Success ";
                event.event(EventType.UPDATE_PROFILE).user(userModel).realm("tmax").detail("username", userName).detail("userWithdrawal","t").success(); //FIXME
            } else{
                status = Status.FORBIDDEN;
                out = "User [" + userName + "] is Unqualified to Withdraw from Account due to [" + unQualifiedReason + "] Policy, Check Withdrawal Policy or Contact Administrator";
            }
        } catch (Exception e) {
            status = Status.BAD_REQUEST;
            out = "User [" + userName + "] Withdrawal Falied  ";
        } catch (Throwable throwable) {
            status = Status.BAD_REQUEST;
            out = "User [" + userName + "] Withdrawal Request Falied  ";
        }
        return Util.setCors(status, out);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        System.out.println("***** GET /test");
        return Util.setCors( Status.OK, null);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /test");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }
}