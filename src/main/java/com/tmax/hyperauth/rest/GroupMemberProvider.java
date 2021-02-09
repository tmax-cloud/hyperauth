package com.tmax.hyperauth.rest;

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
import org.keycloak.policy.PasswordPolicyNotMetException;
import org.keycloak.protocol.oidc.TokenManager.NotBeforeCheck;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.ErrorResponse;
import org.keycloak.services.ErrorResponseException;
import org.keycloak.services.Urls;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resources.admin.UserResource;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.*;
import org.keycloak.models.utils.ModelToRepresentation;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class GroupMemberProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public GroupMemberProvider(KeycloakSession session) {
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
    public Response POST(List< UserRepresentation > reps, @QueryParam("token") String tokenString) {
        System.out.println("***** POST /GroupMember");

        RealmModel realm = session.realms().getRealmByName("tmax");

        clientConnection = session.getContext().getConnection();
        EventBuilder event = new EventBuilder(realm, session, clientConnection);
        String groupAdminName = "";
        try {
            verifyToken(tokenString, realm);
            groupAdminName = token.getPreferredUsername();
            System.out.println("groupAdminName : " + groupAdminName);
        } catch (Exception e) {
            e.printStackTrace();
            out = "token is invalid";
            status = Status.BAD_REQUEST;
            return Util.setCors(status, out);
        }

        List< String > isAdmin = null;
        if (token.getOtherClaims().get("isAdmin") != null && token.getOtherClaims().get("isAdmin").toString() != null){
            isAdmin = Arrays.asList(token.getOtherClaims().get("isAdmin").toString().split(","));
        } else {
            throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
        }
        List < String > groupNameList = null;
        for ( String groupName : isAdmin){
            System.out.println("User [ " + groupAdminName + " ] is Admin of group [ " + groupName + " ]");
            if ( realm.searchForGroupByName(groupName, 0, realm.getGroupsCount(false).intValue()) != null ){
                GroupModel group = realm.searchForGroupByName(groupName, 0, realm.getGroupsCount(false).intValue()).get(0);
                if (groupNameList == null) groupNameList = new ArrayList<>();
                groupNameList.add(group.getName());
            }
        }

        if(groupNameList == null){
            throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
        }

        // For Validation
        if ( reps != null && reps.size() > 0){
            // For userName duplicate check
            HashSet<String> userNameSet = new HashSet<String>();
            for ( UserRepresentation rep : reps ) {
                if (!groupNameList.containsAll(rep.getGroups())) {
                    throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
                }

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
                    UserModel user = null;
                    String username = rep.getUsername();
                    if (realm.isRegistrationEmailAsUsername()) {
                        username = rep.getEmail();
                    }
                    System.out.println("User [ " + username + " ] Register Start");
                    user = session.users().addUser(realm, username);
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
        if (session.getTransactionManager().isActive()) {  // If commit every time, Error occurred
            session.getTransactionManager().commit();
        }

        out = "Group Member Register Success";
        status = Status.OK;
        return Util.setCors(status, out);
    }


	@GET
    @Path("{group}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("group") final String group, @QueryParam("token") String tokenString) {
        System.out.println("***** GET /GroupMember");

        List < UserRepresentation > userOutList = new ArrayList<>();
        UserRepresentation userOut = new UserRepresentation();
    	System.out.println("group : " + group);
        RealmModel realm = session.getContext().getRealm();
        String realmName = realm.getDisplayName();
        if (realmName == null) {
        	realmName = realm.getName();
        }

        String groupAdminName = "";
        try {
            verifyToken(tokenString, realm);
            groupAdminName = token.getPreferredUsername();
            System.out.println("groupAdminName : " + groupAdminName);

            List< String > isAdmin = null;
            if (token.getOtherClaims().get("isAdmin") != null && token.getOtherClaims().get("isAdmin").toString() != null){
                isAdmin = Arrays.asList(token.getOtherClaims().get("isAdmin").toString().split(","));
            } else {
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
            }
            List < String > groupNameList = null;
            for ( String groupName : isAdmin){
                System.out.println("User [ " + groupAdminName + " ] is Admin of group [ " + groupName + " ]");
                if ( realm.searchForGroupByName(groupName, 0, realm.getGroupsCount(false).intValue()) != null ){
                    GroupModel groupModel = realm.searchForGroupByName(groupName, 0, realm.getGroupsCount(false).intValue()).get(0);
                    if (groupNameList == null) groupNameList = new ArrayList<>();
                    groupNameList.add(groupModel.getName());
                }
            }

            if(groupNameList == null){
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
            }
            if ( !groupNameList.contains(group)){
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out = "token is invalid";
            status = Status.BAD_REQUEST;
            return Util.setCors(status, out);
        }

        try {
            GroupModel groupModel = realm.searchForGroupByName(group, 0, realm.getGroupsCount(false).intValue()).get(0);
            List <UserModel> userModelList = session.users().getGroupMembers(realm, groupModel);

            for (UserModel userModel : userModelList){
                userOutList.add(ModelToRepresentation.toRepresentation(session, realm, userModel));
            }
            status = Status.OK;
        	return Util.setCors(status, userOutList);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
            System.out.println("GroupMember ListGet Failed");
        	status = Status.BAD_REQUEST;
        	out = "GroupMember ListGet Failed";
        	return Util.setCors(status, out);
        }  
    }

    @PUT
    @Path("{group}")
    @QueryParam("token")
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("group") final String group,@QueryParam("userName") String userName, @QueryParam("token") String tokenString, UserRepresentation rep) {
        System.out.println("group : " + group);
        System.out.println("userName : " + userName);
//        System.out.println("token : " + tokenString);
        RealmModel realm = session.getContext().getRealm();
        clientConnection = session.getContext().getConnection();
        EventBuilder event = new EventBuilder(realm, session, clientConnection);

        String groupAdminName = "";
        try {
            verifyToken(tokenString, realm);
            groupAdminName = token.getPreferredUsername();
            System.out.println("groupAdminName : " + groupAdminName);

            List< String > isAdmin = null;
            if (token.getOtherClaims().get("isAdmin") != null && token.getOtherClaims().get("isAdmin").toString() != null){
                isAdmin = Arrays.asList(token.getOtherClaims().get("isAdmin").toString().split(","));
            } else {
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
            }
            List < String > groupNameList = null;
            for ( String groupName : isAdmin){
                System.out.println("User [ " + groupAdminName + " ] is Admin of group [ " + groupName + " ]");
                if ( realm.searchForGroupByName(groupName, 0, realm.getGroupsCount(false).intValue()) != null ){
                    GroupModel groupModel = realm.searchForGroupByName(groupName, 0, realm.getGroupsCount(false).intValue()).get(0);
                    if (groupNameList == null) groupNameList = new ArrayList<>();
                    groupNameList.add(groupModel.getName());
                }
            }
            if(groupNameList == null){
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
            }
            if ( !groupNameList.contains(group)){
                throw new ErrorResponseException(OAuthErrorException.INVALID_TOKEN, "No Authorization", Status.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "token is invalid";
            status = Status.BAD_REQUEST;
            return Util.setCors(status, out);
        }
        session.getContext().setClient(clientModel);

        if (!clientModel.isEnabled()) {
            status = Status.BAD_REQUEST;
            out = "Disabled Client ";
        } else {
            GroupModel groupModel = realm.searchForGroupByName(group, 0, realm.getGroupsCount(false).intValue()).get(0);
            List <UserModel> userModelList = session.users().getGroupMembers(realm, groupModel);
            UserModel userModel = null;
            for (UserModel user : userModelList){
                if ( user.getUsername().equalsIgnoreCase(userName) ){
                    userModel = user;
                }
            }
            if (userModel == null) {
                out = "User not found in Group";
                throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "User not found in Group", Status.BAD_REQUEST);
            }
            userModel = session.users().getUserByUsername(userName, realm); // 이유는 모르지만 다시 이걸로 가져오지 않으면, DB는 update되는데 session이 update가 느림.
            try {
                for ( String key : rep.getAttributes().keySet()){
                    System.out.println("[key] : " + key  + " || [value] : "+userModel.getAttribute(key) + " ==> " + rep.getAttributes().get(key));
                    userModel.removeAttribute(key);
                    userModel.setAttribute(key, rep.getAttributes().get(key));
                }
                event.event(EventType.UPDATE_PROFILE).user(userModel).realm("tmax").detail("username", userName).success();
                status = Status.OK;
               out = " GroupMember [" + userName + "] Update Success ";
            } catch (Exception e) {
                status = Status.BAD_REQUEST;
                out = "GroupMember [" + userName + "] Update Falied  ";
            }
        }
        return Util.setCors(status, out);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /GroupMember");
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