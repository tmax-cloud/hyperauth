package com.tmax.hyperauth.authenticator.externalProvider;


import lombok.extern.slf4j.Slf4j;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.ClientSessionCode;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;



/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class InitechAuthenticator implements Authenticator {
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        log.info("user [ "+ context.getUser().getUsername() + " ] Initech Authenticator Start");

        String accessCode = new ClientSessionCode<>(context.getSession(), context.getRealm(), context.getAuthenticationSession()).getOrGenerateCode();
        String clientId = context.getAuthenticationSession().getClient().getClientId();
        String tabId = context.getAuthenticationSession().getTabId();
        String execution = context.getExecution().getId();
        String realm = context.getRealm().getName();
        URI location;
        try {
            location = new URI("http://localhost:8081/externalauth?realm="+realm+"&session_code="+accessCode+"&tab_id="+tabId+"&client_id="+clientId+"&execution="+execution);
            Response response = Response.seeOther(location)
                    .build();
            log.info("Redirecting to " +  location);
            context.forceChallenge(response);
            return;
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void action(AuthenticationFlowContext context) {
        log.info("ExternalAuth Action Called !!!!!!!");
        context.success();
    }


    @Override
    public boolean requiresUser() { return true; }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) { return true; }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) { }

    @Override
    public void close() { }
}
