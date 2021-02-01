package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.caller.StringUtil;
import com.tmax.hyperauth.jpa.EmailVerification;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.OAuthErrorException;
import org.keycloak.TokenVerifier;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.VerificationException;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.crypto.SignatureVerifierContext;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.*;
import org.keycloak.models.utils.CredentialValidation;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.ErrorResponseException;
import org.keycloak.services.Urls;
import org.keycloak.services.managers.BruteForceProtector;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class PasswordProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpResponse response;

    @Context
    private ClientConnection clientConnection;

    public PasswordProvider(KeycloakSession session) {
        this.session = session;
    }
    
    @Override
    public Object getResource() {
        return this;
    }
    private AccessToken token;
    private ClientModel clientModel;

    Connection conn = null;
    long time = System.currentTimeMillis();
    Status status = null;
	String out = null;

	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("email") final String email, @QueryParam("code") String code ,@QueryParam("token") String tokenString,
                        @QueryParam("password") String password, @QueryParam("confirmPassword") String confirmPassword) {
        System.out.println("***** PUT /password");
        try {
            if (StringUtil.isEmpty(email)){
                status = Status.BAD_REQUEST;
                out = "Email Empty";
                return Util.setCors(status, out);
            }
            System.out.println("email : " + email);

            RealmModel realm = session.realms().getRealmByName("tmax");
            clientConnection = session.getContext().getConnection();
            EventBuilder event = new EventBuilder(realm, session, clientConnection); // FIXME

            List< EmailVerification > emailCodeList = null;

            boolean isVerified = false;

            if ( StringUtil.isNotEmpty(code) && StringUtil.isEmpty(tokenString)){
                emailCodeList = getEntityManager().createNamedQuery("findByEmail", EmailVerification.class)
                        .setParameter("email", email).getResultList();
                for ( EmailVerification emailCode : emailCodeList ){
                    if (code.equalsIgnoreCase(emailCode.getCode()) && emailCode.getIsVerified()) {
                        isVerified = true;
                        break;
                    }
                }
            } else if (StringUtil.isNotEmpty(tokenString) && StringUtil.isEmpty(code)){
                verifyToken(tokenString, realm);
                if (!token.getPreferredUsername().equalsIgnoreCase(email)) {
                    out = "Cannot change other user's password";
                    throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "Cannot change other user's password", Response.Status.BAD_REQUEST);
                }
                isVerified = true;
            } else {

            }
            System.out.println("isVerified : " + isVerified);
            // Validation
            if (!isVerified) {
                status = Status.UNAUTHORIZED;
                out = "Unauthorized User";
                return Util.setCors(status, out);
            }
            if (StringUtil.isEmpty(password)) {
                status = Status.BAD_REQUEST;
                out = "Empty Password";
                return Util.setCors(status, out);
            } else if (!Pattern.compile("^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[$@$!%*?&])|(?=.*[a-z])(?=.*\\d)(?=.*[$@$!%*?&])|(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&]))[A-Za-z\\d$@$!%*?&]{9,20}$")
                    .matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: violate the password policy";
                return Util.setCors(status, out);
            } else if (!password.equalsIgnoreCase(confirmPassword)){
                status = Status.BAD_REQUEST;
                out = "Password and confirmation does not match. ";
                return Util.setCors(status, out);
            }

            // Change Password
            System.out.println("Change Password to " + password);
            session.userCredentialManager().updateCredential(session.realms().getRealmByName("tmax"),
                    session.users().getUserByEmail(email, session.realms().getRealmByName("tmax")),
                    UserCredentialModel.password(password, false));
            System.out.println("Change Password Success");

            // If Locked, Disable Temporary lock
            UserModel userModel = session.users().getUserByEmail(email, session.realms().getRealmByName("tmax"));
            if (session.getProvider(BruteForceProtector.class).isTemporarilyDisabled(session, realm, userModel)){
                UserLoginFailureModel loginFailureModel = session.sessions()
                        .getUserLoginFailure(realm, session.users().getUserByEmail(email, session.realms().getRealmByName("tmax")).getId());
                loginFailureModel.clearFailures();
            }

            // Remove DB data
            if ( StringUtil.isNotEmpty(code) && StringUtil.isEmpty(tokenString)){
                for ( EmailVerification emailCode : emailCodeList ){
                    getEntityManager().remove(emailCode);
                }
                System.out.println("delete DB data Success");
            }

            // Event Publish
            event.event(EventType.UPDATE_PASSWORD).user(userModel).realm("tmax").detail("username", userModel.getUsername()).success(); // FIXME


            status = Status.OK;
            out = "Reset Password Success";

            return Util.setCors(status, out);

        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
        	status = Status.BAD_REQUEST;
        	out = "Reset Password Failed";
            return Util.setCors(status, out);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get( @QueryParam("userId") String userId,  @QueryParam("password") String password) {
        System.out.println("***** Verify /password");
        if ( StringUtil.isEmpty(userId)){
            status = Status.BAD_REQUEST;
            out = "User Id is Empty";
            return Util.setCors(status, out);
        }

        if ( StringUtil.isEmpty(password)){
            status = Status.BAD_REQUEST;
            out = "Password is Empty";
            return Util.setCors(status, out);
        }
        RealmModel realm = session.realms().getRealmByName("tmax");
        UserModel user = session.users().getUserByEmail(userId, realm);
        UserCredentialModel cred = UserCredentialModel.password(password);
        if (session.userCredentialManager().isValid(realm, user, cred)) {
            status = Status.OK;
            out = "Password is Correct";
        } else {
            status = Status.BAD_REQUEST;
            out = "Password is Wrong";
        }
        return Util.setCors(status, out);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response other() {
        System.out.println("***** OPTIONS /password");
        return Util.setCors( Status.OK, null);
    }

    @Override
    public void close() {
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    private void verifyToken(String tokenString, RealmModel realm) throws VerificationException {
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
                .withChecks(TokenManager.NotBeforeCheck.forModel(clientModel))
                .verify();
    }
}
