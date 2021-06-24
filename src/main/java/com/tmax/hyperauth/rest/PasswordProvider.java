package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.caller.StringUtil;
import com.tmax.hyperauth.jpa.EmailVerification;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.OAuthErrorException;
import org.keycloak.TokenVerifier;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.VerificationException;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.crypto.SignatureVerifierContext;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.*;
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
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class PasswordProvider implements RealmResourceProvider {
    @Context
    private KeycloakSession session;

    @Context
    private HttpRequest request;

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

    Status status = null;
	String out = null;

	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response put(@QueryParam("email") String email, @QueryParam("code") String code ,@QueryParam("token") String tokenString,
                        @FormParam("password") String password, @FormParam("confirmPassword") String confirmPassword) {
        log.info("***** PUT /password");

            if (StringUtil.isEmpty(email)){
                status = Status.BAD_REQUEST;
                out = "Email Empty";
                return Util.setCors(status, out);
            }
            log.info("email : " + email);

            RealmModel realm = session.getContext().getRealm();
            clientConnection = session.getContext().getConnection();
            EventBuilder event = new EventBuilder(realm, session, clientConnection); // FIXME

            List< EmailVerification > emailCodeList = null;
            boolean isVerified = false;
            String username = email; // 일단 이메일을 username으로 간주한다.

            if ( StringUtil.isNotEmpty(code) && StringUtil.isEmpty(tokenString)){
                emailCodeList = getEntityManager().createNamedQuery("findByEmail", EmailVerification.class)
                        .setParameter("email", username).getResultList();
                for ( EmailVerification emailCode : emailCodeList ){
                    if (code.equalsIgnoreCase(emailCode.getCode()) && emailCode.getIsVerified()) {
                        isVerified = true;
                        break;
                    }
                }
            } else if (StringUtil.isNotEmpty(tokenString) && StringUtil.isEmpty(code)){
                try{
                    verifyToken(tokenString, realm);
                }catch(Exception e){
                    status = Status.UNAUTHORIZED;
                    out = "Unauthorized User";
                    return Util.setCors(status, out);
                }
                if (!token.getEmail().equalsIgnoreCase(username)) {
                    out = "Cannot change other user's password";
                    return Util.setCors(status, out);
                }
                isVerified = true;
                username = token.getPreferredUsername(); // token 방식으로 비밀번호를 변경할때는 유저를 username으로 찾는게 확실히 특정할 수 잇다.
            }

            // Validation
            log.info("isVerified : " + isVerified);
            if (!isVerified) {
                status = Status.UNAUTHORIZED;
                out = "Unauthorized User";
                return Util.setCors(status, out);
            }

            if (StringUtil.isEmpty(password)) {
                status = Status.BAD_REQUEST;
                out = "Empty Password";
                return Util.setCors(status, out);
            } else if (!Pattern.compile(AuthenticatorConstants.TMAX_REALM_PASSWORD_POLICY)
                    .matcher(password).matches()) {
                status = Status.BAD_REQUEST;
                out = "Invalid password: violate the password policy";
                return Util.setCors(status, out);
            } else if (!password.equalsIgnoreCase(confirmPassword)){
                status = Status.BAD_REQUEST;
                out = "Password and confirmation does not match";
                return Util.setCors(status, out);
            } else if (sameWithOldPW(username, password)){
                status = Status.BAD_REQUEST;
                out = "sameWithOldPassword";
                return Util.setCors(status, out);
            }

        try {
            // Change Password
            log.debug("Change Password to " + password);
            session.userCredentialManager().updateCredential(realm,
                    session.users().getUserByUsername(username, realm),
                    UserCredentialModel.password(password, false));
            log.info("Change Password Success");

            // If Locked, Disable Temporary lock
            UserModel userModel = session.users().getUserByUsername(username, realm);
            if (session.getProvider(BruteForceProtector.class).isTemporarilyDisabled(session, realm, userModel)){
                UserLoginFailureModel loginFailureModel = session.sessions()
                        .getUserLoginFailure(realm, session.users().getUserByUsername(username, realm).getId());
                loginFailureModel.clearFailures();
            }

            // Remove DB data
            if ( StringUtil.isNotEmpty(code) && StringUtil.isEmpty(tokenString)){
                for ( EmailVerification emailCode : emailCodeList ){
                    getEntityManager().remove(emailCode);
                }
                log.info("delete DB data Success");
            }

            // Event Publish
            event.event(EventType.UPDATE_PASSWORD).user(userModel).realm(realm).detail("username", userModel.getUsername()).success(); // FIXME

            status = Status.OK;
            out = "Reset Password Success";
            return Util.setCors(status, out);

        }catch (Exception e) {
            log.error("Error Occurs!!", e);
            status = Status.BAD_REQUEST;
        	out = "Reset Password Failed";
            return Util.setCors(status, out);
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response patch( @QueryParam("userId") String userId, @FormParam("password") String password) {
        log.info("***** Verify ( patch ) /password");
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
        RealmModel realm = session.getContext().getRealm();
        UserModel user = session.users().getUserByUsername(userId, realm);
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
        log.info("***** OPTIONS /password");
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

    private boolean sameWithOldPW(String username, String password) {
        UserCredentialModel cred = UserCredentialModel.password(password);
        if (session.userCredentialManager().isValid(session.getContext().getRealm(), session.users().getUserByUsername(username, session.getContext().getRealm()), cred)) {
            return true;
        } else {
            return false;
        }
    }
}
