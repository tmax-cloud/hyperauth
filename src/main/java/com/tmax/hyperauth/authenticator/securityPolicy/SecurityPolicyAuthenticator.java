package com.tmax.hyperauth.authenticator.securityPolicy;

import org.keycloak.authentication.*;
import org.keycloak.models.*;
import javax.ws.rs.core.Response;

/**
 * @author taegeon_woo@tmax.co.kr
 */
public class SecurityPolicyAuthenticator implements Authenticator {

    protected boolean isSecurityPolicyEnabled(AuthenticationFlowContext context) {
        boolean flag = false;
        String isSecurityPolicyEnabled = SecurityPolicyAuthenticatorUtil.getAttributeValue(context.getUser(), "ipBlock");
        System.out.println("isSecurityPolicyEnabled From Attribute : " + isSecurityPolicyEnabled);
        if (isSecurityPolicyEnabled != null && isSecurityPolicyEnabled.equalsIgnoreCase("true")){
            flag = true;
        }
        return flag;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        System.out.println("authenticate called ... User = " + context.getUser().getUsername());

        if (!isSecurityPolicyEnabled(context)) {
            System.out.println("Bypassing Security Policy since disabled user [ " + context.getUser().getUsername() +" ]");
            context.challenge(context.form().createForm("test.ftl"));
            context.success();
            return;
        }else {
            Response challenge =  context.form()
                    .setError("Mobile number can not be determined.").createForm("security-policy-validation-error.ftl");
            context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
            System.out.println("Blocked by Security Policy [ " + context.getUser().getUsername() +" ]");
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        System.out.println("action called ... context = " + context);
        context.success();
    }


    @Override
    public boolean requiresUser() {
        System.out.println("requiresUser called ... returning true");
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        System.out.println("configuredFor called ... session=" + session + ", realm=" + realm + ", user=" + user);
        System.out.println("... returning true");
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        System.out.println("setRequiredActions called ... session=" + session + ", realm=" + realm + ", user=" + user);
    }

    @Override
    public void close() {
        System.out.println("close called ...");
    }

}
