package com.tmax.hyperauth.rest;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tmax.hyperauth.caller.Constants;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.OAuthErrorException;
import org.keycloak.TokenVerifier;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.VerificationException;
import org.keycloak.common.util.ObjectUtil;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.crypto.SignatureVerifierContext;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.*;
import org.keycloak.models.utils.RepresentationToModel;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.policy.PasswordPolicyNotMetException;
import org.keycloak.protocol.oidc.TokenManager.NotBeforeCheck;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.ErrorResponse;
import org.keycloak.services.ErrorResponseException;
import org.keycloak.services.Urls;
import org.keycloak.services.resource.RealmResourceProvider;

import com.tmax.hyperauth.caller.HypercloudOperatorCaller;
import org.keycloak.services.resources.admin.UserResource;


/**
 * @author taegeon_woo@tmax.co.kr
 */

public class UserProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;
   
    @Context
    private HttpResponse response;
   
    @Context
    private ClientConnection clientConnection;

    public UserProvider(KeycloakSession session) {
        this.session = session;
    }
    private AccessToken token;
    private ClientModel clientModel;

    @Override
    public Object getResource() {
        return this;
    }

    Status status = null;
	String out = null;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response POST(List< UserRepresentation > reps) {
        System.out.println("***** POST /User");

        RealmModel realm = session.realms().getRealmByName("tmax");

        clientConnection = session.getContext().getConnection();
        EventBuilder event = new EventBuilder(realm, session, clientConnection); // FIXME

        // For Validation
        if ( reps != null && reps.size() > 0){
            // For userName duplicate check
            HashSet<String> userNameSet = new HashSet<String>();
            for ( UserRepresentation rep : reps ) {
                String username = rep.getUsername();
                if (realm.isRegistrationEmailAsUsername()) {
                    username = rep.getEmail();
                }
                if (ObjectUtil.isBlank(username)) {
                    return ErrorResponse.error("User name is missing", Status.BAD_REQUEST);
                }

                // Double-check duplicated username and email here due to federation
                if (session.users().getUserByUsername(username, realm) != null) {
                    return ErrorResponse.exists("User exists with same username");
                }
                if (rep.getEmail() != null && !realm.isDuplicateEmailsAllowed()) {
                    try {
                        if (session.users().getUserByEmail(rep.getEmail(), realm) != null) {
                            return ErrorResponse.exists("User exists with same email");
                        }
                    } catch (ModelDuplicateException e) {
                        return ErrorResponse.exists("User exists with same email");
                    }
                }
                userNameSet.add(rep.getEmail()); // FIXME : For Policy, check Email but could be change
            }
            if ( reps.size() != userNameSet.size()){
                return ErrorResponse.exists("User Email Duplicated");
            }

            // For Logic
            for ( UserRepresentation rep : reps ){
                try {
                    String username = rep.getUsername();
                    if (realm.isRegistrationEmailAsUsername()) {
                        username = rep.getEmail();
                    }
                    System.out.println("User [ " + username + " ] Register Start");
                    UserModel user = session.users().addUser(realm, username);
                    Set<String> emptySet = Collections.emptySet();
                    UserResource.updateUserFromRep(user, rep, emptySet, realm, session, false);
                    RepresentationToModel.createFederatedIdentities(rep, session, realm, user);
                    RepresentationToModel.createGroups(rep, realm, user);
                    RepresentationToModel.createCredentials(rep, session, realm, user, true);
                    System.out.println("User [ " + username + " ] Register Success");

                    event.event(EventType.REGISTER).user(user).realm("tmax").detail("username", username).success(); // FIXME

                } catch (ModelDuplicateException e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    return ErrorResponse.exists("User exists with same username or email");
                } catch (PasswordPolicyNotMetException e) {
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    return ErrorResponse.error("Password policy not met", Status.BAD_REQUEST);
                } catch (ModelException me){
                    if (session.getTransactionManager().isActive()) {
                        session.getTransactionManager().setRollbackOnly();
                    }
                    System.out.println("Could not create user");
                    return ErrorResponse.error("Could not create user", Status.BAD_REQUEST);
                }
            }
        } else {
            return ErrorResponse.exists("No User to Register");
        }

        if (session.getTransactionManager().isActive()) {
            session.getTransactionManager().commit();
        }

        out = "user Register Success";
        status = Status.OK;
        return Util.setCors(status, out);
    }


	@GET
    @Path("{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("userName") final String userName) {
        System.out.println("***** GET /User");

        UserRepresentation userOut = new UserRepresentation();
    	System.out.println("userName request : " + userName);

        RealmModel realm = session.getContext().getRealm();

        String realmName = realm.getDisplayName();
        if (realmName == null) {
        	realmName = realm.getName();
        }
        List <String> groupName = null;
        try {
            UserModel user = session.users().getUserByUsername(userName, session.realms().getRealmByName(realmName));

            if (user == null) {
                status = Status.BAD_REQUEST;
                out = "No Corresponding UserName";
                return Util.setCors(status, out);
            }
            System.out.println("email : " + user.getEmail());

            for( GroupModel group : user.getGroups()) {
        		if(groupName == null) groupName = new ArrayList<>();
        		groupName.add(group.getName());
            	System.out.println("groupName : " + group.getName());
        	}

        	userOut.setUsername(userName);
        	userOut.setEmail(user.getEmail());
        	userOut.setGroups(groupName);
            userOut.setEnabled(user.isEnabled());

            // User Credential Data
            if( session.userCredentialManager().getStoredCredentialsByType(realm, user, "password")!= null
                    && session.userCredentialManager().getStoredCredentialsByType(realm, user, "password").get(0) != null ){
                List <CredentialRepresentation> credentials = new ArrayList<>();
                CredentialRepresentation credential =  ModelToRepresentation.
                        toRepresentation( session.userCredentialManager().getStoredCredentialsByType(realm, user, "password").get(0) );
                credentials.add(credential);
                userOut.setCredentials(credentials);
            }

            // User Attribute Data
            userOut.setAttributes(user.getAttributes());

            // FIXME : 로그인 실패시 실패 정보를 보여주려고 의도한 건데, 자꾸 user Attribute에 저장되는 현상 발생, 아직 미해결
//        	// Login Failure Data
//            UserLoginFailureModel loginFailureModel = session.sessions().getUserLoginFailure(realm, user.getId());
//            if ( loginFailureModel != null ){
//                boolean disabled;
//                if (user == null) {
//                    disabled = Time.currentTime() < loginFailureModel.getFailedLoginNotBefore();
//                } else {
//                    disabled = session.getProvider(BruteForceProtector.class).isTemporarilyDisabled(session, realm, user);
//                }
//                // User Attribute Data
//                userOut.getAttributes().put("temporarilyDisabled", new ArrayList<>(Arrays.asList(String.valueOf(disabled))));
//                userOut.getAttributes().put("numFailures", new ArrayList<>(Arrays.asList(Integer.toString(loginFailureModel.getNumFailures()))));
//                userOut.getAttributes().put("lastIPFailure", new ArrayList<>(Arrays.asList(loginFailureModel.getLastIPFailure())));
//                userOut.getAttributes().put("lastFailure", new ArrayList<>(Arrays.asList(Long.toString(loginFailureModel.getLastFailure()))));
//                userOut.getAttributes().put("failedLoginNotBefore", new ArrayList<>(Arrays.asList(Integer.toString(loginFailureModel.getFailedLoginNotBefore()))));
//                userOut.getAttributes().put("remainSecond", new ArrayList<>(Arrays.asList( Long.toString( loginFailureModel.getFailedLoginNotBefore() - loginFailureModel.getLastFailure()/1000 ))));
//            }

            status = Status.OK;
        	return Util.setCors(status, userOut);        
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("No Corresponding UserName");
            System.out.println("Exception " + e.getMessage());
        	status = Status.BAD_REQUEST;
        	out = "No Corresponding UserName";
        	return Util.setCors(status, out);
        }  
    }
    
    @SuppressWarnings("unchecked")
	@DELETE
    @Path("{userName}")
    @QueryParam("token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("userName") final String userName, @QueryParam("token") String tokenString ) {
        System.out.println("***** DELETE /User");

        System.out.println("userName : " + userName);
        System.out.println("token : " + tokenString);
        RealmModel realm = session.getContext().getRealm();
        clientConnection = session.getContext().getConnection();
        EventBuilder event = new EventBuilder(realm, session, clientConnection).detail("username", userName); // FIXME

        try {
            verifyToken(tokenString, realm);
            if (!token.getPreferredUsername().equalsIgnoreCase(userName)) {
                out = "Cannot delete other user";
                throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Cannot delete other user", Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = Status.BAD_REQUEST;
            return Util.setCors(status, out);
        }

        session.getContext().setClient(clientModel);

        if (!clientModel.isEnabled()) {
            status = Status.BAD_REQUEST;
            out = "Disabled Client ";
        } else {
            String realmName = realm.getDisplayName();
            if (realmName == null) {
                realmName = session.getContext().getRealm().getName();
            }
            UserModel userModel = session.users().getUserByUsername(userName, session.realms().getRealmByName(realmName));

            try {
                if (userModel == null) {
                    out = "User not found";
                    throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "User not found", Response.Status.BAD_REQUEST);
                }
            } catch (Exception e) {
                status = Status.BAD_REQUEST;
                out = "User not found";
                return Util.setCors(status, out);
            }

            try {
                session.users().removeUser(realm, userModel);
                event.event(EventType.UPDATE_PROFILE).user(userModel).realm("tmax").detail("username", userName).detail("userDelete","t").success(); //FIXME
                System.out.println("Delete user role in k8s");
                HypercloudOperatorCaller.deleteNewUserRole(userName);

                status = Status.OK;
                out = " User [" + userName + "] Delete Success ";
            } catch (Exception e) {
                status = Status.BAD_REQUEST;
                out = "User [" + userName + "] Delete Falied  ";
            }
        }
        return Util.setCors(status, out);
    }


    @PUT
    @Path("{userName}")
    @QueryParam("token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("userName") final String userName, @QueryParam("token") String tokenString,  @QueryParam("withdrawal") String withdrawal , UserRepresentation rep) {
        System.out.println("***** PUT /User");

        System.out.println("userName : " + userName);
        System.out.println("token : " + tokenString);
        System.out.println("withdrawal : " + withdrawal);
        RealmModel realm = session.getContext().getRealm();
        clientConnection = session.getContext().getConnection();
        EventBuilder event = new EventBuilder(realm, session, clientConnection); // FIXME

        try {
            verifyToken(tokenString, realm);

            if (!token.getPreferredUsername().equalsIgnoreCase(userName)) {
                out = "Cannot update other user";
                throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Cannot update other user", Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = Status.BAD_REQUEST;
            return Util.setCors(status, out);
        }

        session.getContext().setClient(clientModel);

        if (!clientModel.isEnabled()) {
            status = Status.BAD_REQUEST;
            out = "Disabled Client ";
        } else {
            String realmName = realm.getDisplayName();
            if (realmName == null) {
                realmName = session.getContext().getRealm().getName();
            }
            UserModel userModel = session.users().getUserByUsername(userName, session.realms().getRealmByName(realmName));

            try {
                if (userModel == null) {
                    out = "User not found";
                    throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "User not found", Response.Status.BAD_REQUEST);
                }
            } catch (Exception e) {
                status = Status.BAD_REQUEST;
                out = "User not found";
                return Util.setCors(status, out);
            }

            try {
                if (withdrawal != null && withdrawal.equalsIgnoreCase("t")){
                    // 유저 탈퇴 신청 API
                    // Withdrawal Qualification Validation
                    boolean isQualified = true;
                    String unQualifiedReason = null;
                    if(userModel.getAttributes()!=null) {
                        for (String key : userModel.getAttributes().keySet()) {
                            if ( key.startsWith( "withdrawal_unqualified_") && userModel.getAttribute(key).toString().equalsIgnoreCase("t")){
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

                        if(userModel.getAttributes()!=null) userModel.removeAttribute("deletionDate");
                        userModel.setAttribute("deletionDate", Arrays.asList(deletionDateString));
                        userModel.setEnabled(false);
                        String email = userModel.getEmail();
                        String subject = "[Tmax 통합서비스] 고객님의 계정 탈퇴 신청이 완료되었습니다.";
                        String msg = Constants.ACCOUNT_WITHDRAWAL_REQUEST_BODY;
                        Util.sendMail(session, email, subject, msg, null, null );
                        out = " User [" + userName + "] WithDrawal Request Success ";
                        event.event(EventType.UPDATE_PROFILE).user(userModel).realm("tmax").detail("username", userName).detail("userWithdrawal","t").success(); //FIXME
                    } else{
                        status = Status.FORBIDDEN;
                        out = "User [" + userName + "] is Unqualified to Withdraw from Account due to [" + unQualifiedReason + "] Policy, Check Withdrawal Policy or Contact Administrator";
                    }
                } else if (withdrawal != null && withdrawal.equalsIgnoreCase("f")) {
                    // 유저 탈퇴 신청 철회 API
                    userModel.setEnabled(true);
                    if ( userModel.getAttributes() != null) userModel.removeAttribute("deletionDate");
                    event.event(EventType.UPDATE_PROFILE).user(userModel).realm("tmax").detail("username", userName).detail("userWithdrawal","f").success(); //FIXME
                }else {
                    // 유저 Attribute Update API
                    for ( String key : rep.getAttributes().keySet()) {
                        System.out.println("[key] : " + key + " || [value] : " + userModel.getAttribute(key) + " ==> " + rep.getAttributes().get(key));
                        userModel.removeAttribute(key);
                        userModel.setAttribute(key, rep.getAttributes().get(key));
                    }
                    out = " User [" + userName + "] Update Success ";
                    event.event(EventType.UPDATE_PROFILE).user(userModel).realm("tmax").detail("username", userName).success();
                }
                status = Status.OK;
            } catch (Exception e) {
                status = Status.BAD_REQUEST;
                out = "User [" + userName + "] Update Falied  ";
            } catch (Throwable throwable) {
                status = Status.BAD_REQUEST;
                out = "User [" + userName + "] Withdrawal Request Falied  ";
            }
        }
        return Util.setCors(status, out);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /User");
        return Util.setCors( Status.OK, null);
    }

    private void verifyToken(String tokenString, RealmModel realm) throws VerificationException {
        if (tokenString == null) {
            out = "Token not provided";
            throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Token not provided", Status.BAD_REQUEST);
        }
        TokenVerifier<AccessToken> verifier = TokenVerifier.create(tokenString, AccessToken.class).withDefaultChecks()
                .realmUrl(Urls.realmIssuer(session.getContext().getUri().getBaseUri(), realm.getName()));

        SignatureVerifierContext verifierContext = session.getProvider(SignatureProvider.class,
                verifier.getHeader().getAlgorithm().name()).verifier(verifier.getHeader().getKeyId());
        verifier.verifierContext(verifierContext);
        try {
            token = verifier.verify().getToken();
        } catch (Exception e) {
            out = "token invalid";
            throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "token invalid", Status.UNAUTHORIZED);
        }
        clientModel = realm.getClientByClientId(token.getIssuedFor());
        if (clientModel == null) {
            out = "Client not found";
            throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Client not found", Status.NOT_FOUND);
        }

        TokenVerifier.createWithoutSignature(token)
                .withChecks(NotBeforeCheck.forModel(clientModel))
                .verify();
    }

    @Override
    public void close() {
    }

}