package com.tmax.hyperauth.authenticator.authSelectBox;

        import lombok.extern.slf4j.Slf4j;
        import org.keycloak.authentication.AuthenticationFlowContext;
        import org.keycloak.authentication.Authenticator;
        import org.keycloak.models.*;

        import javax.ws.rs.core.MultivaluedMap;
        import javax.ws.rs.core.Response;

/**
 * @author taegeon_woo@tmax.co.krt
 */

@Slf4j
public class selectAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
            log.info("Select Box!!");
            Response challenge = context.form().createForm("selectBox.ftl");
            context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        if (context.getHttpRequest().getDecodedFormParameters() != null) {
            MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
            String selection = formData.getFirst("selection");
            switch (selection){
                case "o":
                    log.info("Select OTP!!");
//                    context.getSession().setAttribute("selection", "mailOtp");
                    context.getAuthenticationSession().setAuthNote("selection", "mailOtp");

                    break;
                case "p":
                    log.info("Password Form!!");
//                    context.getSession().setAttribute("selection", "password");
                    context.getAuthenticationSession().setAuthNote("selection", "password");

                    break;
                default:
                    log.info("Unknown Auth!!");
                    break;
            }
            context.success();
        }
    }

    @Override
    public boolean requiresUser() { return true; }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) { return true;}

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) { }

    @Override
    public void close() { }
}


