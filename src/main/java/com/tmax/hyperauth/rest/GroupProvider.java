package com.tmax.hyperauth.rest;

import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.OAuthErrorException;
import org.keycloak.TokenVerifier;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.VerificationException;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.crypto.SignatureVerifierContext;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.ErrorResponseException;
import org.keycloak.services.Urls;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class GroupProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public GroupProvider(KeycloakSession session) {
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
    public Response post() {
        log.info("***** POST /group");
        return Util.setCors(status, out);
    }

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("startsWith") String startsWith, @QueryParam("except") List<String> except, @QueryParam("exceptDefault") boolean exceptDefault,  @QueryParam("token") String tokenString) {
        log.info("***** LIST /group");
        List<String> groupListOut;
        log.debug("token : " + tokenString);
        log.info("startsWith request : " + startsWith);
        log.info("except request : " + except);

        try{
            if (!Util.isHyperauthAdmin(session,tokenString)){
                verifyToken(tokenString, session.getContext().getRealm());
                log.info(" User Who Requested Group List : " + token.getPreferredUsername());

                if (!(token.getResourceAccess("realm-management")!= null
                        && token.getResourceAccess("realm-management").getRoles() != null
                        && token.getResourceAccess("realm-management").getRoles().contains("view-users"))){
                    log.error("Exception : UnAuthorized User [ " + token.getPreferredUsername() + " ] to get User List" );
                    status = Status.UNAUTHORIZED;
                    out = "User ListGet Failed";
                    return Util.setCors(status, out);
                }
            }

            StringBuilder query = new StringBuilder();
            query.append("select g.name from GroupEntity g where g.realm = '"+ session.getContext().getRealm().getName() +"'");

            if (startsWith != null){
                startsWith = startsWith.toLowerCase();
                query.append(" and lower(g.name) like '" + startsWith + "%'");
            }

            if (except != null && except.size() > 0){
                query.append(" and not g.name in (");
                for (int i=0 ; i < except.size() ; i ++){
                    query.append("'" + except.get(i) + "'");
                    if (i != except.size()-1){
                        query.append(",");
                    }
                }
                query.append(" )");
            }

            log.info("query : " + query.toString());
            groupListOut = getEntityManager().createQuery(query.toString(), String.class).getResultList();

            if (exceptDefault){
                List<GroupModel> defaultGroups = session.getContext().getRealm().getDefaultGroups();
                if (defaultGroups != null && defaultGroups.size() > 0){
                    groupListOut.removeIf(
                            group ->(
                                    defaultGroups.stream()
                                            .map(GroupModel::getName)
                                            .collect(Collectors.toList())
                                            .contains(group)));
                }
            }
            status = Status.OK;
            return Util.setCors(status, groupListOut);
        }catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
            out = "Group ListGet Failed";
            return Util.setCors(status, out);
        }
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        log.info("***** OPTIONS /group");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
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
            log.error("Error Occurs!!", e);
            out = "token invalid";
            throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "token invalid", Status.UNAUTHORIZED);
        }
        clientModel = realm.getClientByClientId(token.getIssuedFor());
        if (clientModel == null) {
            out = "Client not found";
            throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Client not found", Status.NOT_FOUND);
        }

        TokenVerifier.createWithoutSignature(token)
                .withChecks(TokenManager.NotBeforeCheck.forModel(clientModel))
                .verify();
    }
}