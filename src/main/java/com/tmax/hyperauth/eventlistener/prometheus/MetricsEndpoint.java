package com.tmax.hyperauth.eventlistener.prometheus;

import com.openshift.restclient.authorization.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.resource.RealmResourceProvider;

import javax.naming.AuthenticationException;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.Base64;

@Slf4j
public class MetricsEndpoint implements RealmResourceProvider {

    public final static String ID = "metrics";

    @Context
    private KeycloakSession session;
    public MetricsEndpoint(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {

        // Authentication With Admin ID, Password
        try{
            verifyHyperauthAdmin();
        } catch ( AuthenticationException e){
            return Response.status(401).build();
        }

        // Hyperauth User & Session Count
        PrometheusExporter.instance().recordUserCount(session);
        PrometheusExporter.instance().recordUserSessionCount(session);

        final StreamingOutput stream = output -> PrometheusExporter.instance().export(output);
        return Response.ok(stream).build();
    }

    private void verifyHyperauthAdmin() throws AuthenticationException {
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] authBytes = decoder.decode(session.getContext().getRequestHeaders().getRequestHeader("Authorization").get(0).substring(6).getBytes());
            String[] authInfo = (new String(authBytes)).split(":");
            String username = authInfo[0];
            log.debug("User requested metrics : " + username);
            String password = authInfo[1];

            RealmModel realm = session.realms().getRealmByName("master");
            UserModel user = session.users().getUserByUsername(username, realm);
            UserCredentialModel cred = UserCredentialModel.password(password);
            if (session.userCredentialManager().isValid(realm, user, cred)
                    && user.hasRole(realm.getRole("admin"))) {
                log.debug("Admin User : " + username);
            } else {
                log.error("password is wrong");
                throw new AuthenticationException("Password is Wrong");
            }
        }catch(Exception e){
            log.error(e.getMessage());
            throw new AuthenticationException("Unauthorized User");
        }
    }

    @Override
    public void close() {
    }
}
