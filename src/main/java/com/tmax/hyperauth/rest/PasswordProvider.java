package com.tmax.hyperauth.rest;

import com.tmax.hyperauth.caller.StringUtil;
import com.tmax.hyperauth.jpa.EmailVerification;
import org.jboss.resteasy.spi.HttpResponse;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.models.utils.KeycloakModelUtils;
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

    Connection conn = null;
    long time = System.currentTimeMillis();
    Status status = null;
	String out = null;

	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("email") final String email, @QueryParam("code") String code, @QueryParam("password") String password, @QueryParam("confirmPassword") String confirmPassword) {
        System.out.println("***** PUT /password");
        System.out.println("email : " + email);
        boolean isVerified = false;
        try {
            List< EmailVerification > emailCodeList = getEntityManager().createNamedQuery("findByEmail", EmailVerification.class)
                    .setParameter("email", email).getResultList();
            for ( EmailVerification emailCode : emailCodeList ){
                if (code.equalsIgnoreCase(emailCode.getCode()) && emailCode.getIsVerified()) {
                    isVerified = true;
                    break;
                }
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

            for ( EmailVerification emailCode : emailCodeList ){
                getEntityManager().remove(emailCode);
            }
            System.out.println("delete DB data Success");

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
}