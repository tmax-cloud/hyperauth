package com.tmax.hyperauth.authenticator.authSelectBox;

        import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
        import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
        import com.tmax.hyperauth.rest.Util;
        import org.keycloak.authentication.AuthenticationFlowContext;
        import org.keycloak.authentication.Authenticator;
        import org.keycloak.models.*;

        import javax.ws.rs.core.MultivaluedMap;
        import javax.ws.rs.core.Response;

/**
 * @author taegeon_woo@tmax.co.krt
 */
public class selectAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
            System.out.println("Select Box!!");
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
                    System.out.println("Select OTP!!");
                    context.getSession().setAttribute("selection", "mailOtp");
                    break;
                case "p":
                    System.out.println("Password Form!!");
                    context.getSession().setAttribute("selection", "password");
                    break;
                default:
                    System.out.println("Unknown Auth!!");
                    break;
            }
            context.success();
        }
        context.success();
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


