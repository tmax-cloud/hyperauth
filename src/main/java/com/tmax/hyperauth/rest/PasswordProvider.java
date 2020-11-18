package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.caller.StringUtil;
import com.tmax.hyperauth.jpa.EmailVerification;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.OAuthErrorException;
import org.keycloak.TokenVerifier;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.VerificationException;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.crypto.SignatureVerifierContext;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.utils.KeycloakModelUtils;
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
            } else if (!Pattern.compile("[^ ]{9,}").matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: minimum length 9.";
                return Util.setCors(status, out);
            } else if (!Pattern.compile(".*[a-z].*").matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: must contain at least 1 lower case characters.";
                return Util.setCors(status, out);
            } else if (!Pattern.compile(".*[A-Z].*").matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: must contain at least 1 upper case characters.";
                return Util.setCors(status, out);
            } else if (!Pattern.compile(".*\\d.*").matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: must contain at least 1 numerical digits.";
                return Util.setCors(status, out);
            } else if (!Pattern.compile(".*[!@#$%^&*].*").matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: must contain at least 1 special characters.";
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

            if ( StringUtil.isNotEmpty(code) && StringUtil.isEmpty(tokenString)){
                for ( EmailVerification emailCode : emailCodeList ){
                    getEntityManager().remove(emailCode);
                }
                System.out.println("delete DB data Success");
            }

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