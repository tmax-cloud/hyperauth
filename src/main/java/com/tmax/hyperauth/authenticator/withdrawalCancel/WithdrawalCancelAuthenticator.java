package com.tmax.hyperauth.authenticator.withdrawalCancel;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;
import javax.ws.rs.core.Response;


/**
 * @author taegeon_woo@tmax.co.krt
 */
public class WithdrawalCancelAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        System.out.println("authenticate called ... User = " + context.getUser().getUsername());
        if (AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_DELETION_DATE) != null) {
            System.out.println("User [ " + context.getUser().getUsername() + " ] Need to Withdrawal Cancel to Login");
            Response challenge = context.form().createForm("withdrawal-cancel.ftl");
            context.challenge(challenge);
        } else {
            System.out.println("User [ " + context.getUser().getUsername() + " ] Do Not Need to Withdrawal Check");
            context.success();
            return;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        System.out.println("User [ " + context.getUser().getUsername() + " ] Cancelled withdrawal!!");
        context.getUser().removeAttribute(AuthenticatorConstants.USER_ATTR_DELETION_DATE);
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

