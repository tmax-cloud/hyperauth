package com.tmax.hyperauth.authenticator.passwordUpdateAlert;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import com.tmax.hyperauth.caller.StringUtil;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.*;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author taegeon_woo@tmax.co.kr
 */
public class PasswordUpdateAlertAuthenticator implements Authenticator {

    protected boolean hasTimePassed(AuthenticationFlowContext context, int period) {
        try{
            Long lastPWUpdateTime = (AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_LAST_PW_UPDATE_DATE) != null)?
                    Long.valueOf(AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_LAST_PW_UPDATE_DATE)) : context.getUser().getCreatedTimestamp();
            System.out.println( "lastPWUpdateTime : " + lastPWUpdateTime);
            // 1609228737654   1612023240732
            Long now = System.currentTimeMillis();
            System.out.println( "now : " + now);

            long monthLong = 1000 * 60 * 60 * 24 * 30;  //2,592,000,000   2,794,503,078
            System.out.println( "monthLong : " + monthLong);
            System.out.println( "monthLong * period : " + monthLong * period);
            System.out.println( "now-lastPWUpdateTime : " + (now-lastPWUpdateTime));

            if ((now-lastPWUpdateTime) > monthLong * period){
                System.out.println( "return true");
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        System.out.println("authenticate called ... User = " + context.getUser().getUsername());
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int period = AuthenticatorUtil.getConfigInt(config, AuthenticatorConstants.CONF_PW_UPDATE_PERIOD, 3);
        System.out.println("Using Password Update Period  : " + period + ", user [ "+ context.getUser().getUsername() + " ]");

        if (!hasTimePassed(context, period) ) {
            System.out.println("User [ " + context.getUser().getUsername() + " ] Do Not Need to Update Password");
            context.success();
            return;
        } else {
            System.out.println("User [ " + context.getUser().getUsername() + " ] Need to Update Password");
            Response challenge = context.form().createForm("password-update-alert.ftl");
            context.challenge(challenge);
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        System.out.println("action called ... context = " + context);
        if (context.getHttpRequest().getDecodedFormParameters() != null) {
            MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
            String skip = formData.getFirst(AuthenticatorConstants.PW_UPDATE_SKIP);
            if (skip != null && skip.equalsIgnoreCase("t")) {
                System.out.println("User [ " + context.getUser().getUsername() + " ] Skip Password Update !");
                AuthenticatorConfigModel config = context.getAuthenticatorConfig();
                int updatePeriod = AuthenticatorUtil.getConfigInt(config, AuthenticatorConstants.CONF_PW_UPDATE_PERIOD, 3);
                int skipPeriod = AuthenticatorUtil.getConfigInt(config, AuthenticatorConstants.CONF_PW_UPDATE_SKIP_PERIOD, 1);
                System.out.println("User [ " + context.getUser().getUsername() + " ] Ask Again " + skipPeriod + "Month Later");

                long monthLong = 1000 * 60 * 60 * 24 * 30;
                Long newLastPWUpdateTime = System.currentTimeMillis() - monthLong * (updatePeriod - skipPeriod);
                context.getUser().setAttribute(AuthenticatorConstants.USER_ATTR_LAST_PW_UPDATE_DATE, Arrays.asList(Long.toString(newLastPWUpdateTime)));
                context.success();

            } else {

                System.out.println("User [ " + context.getUser().getUsername() + " ] Insert New Password !");
                String password = formData.getFirst(AuthenticatorConstants.USER_PASSWORD);
//                String confirmPassword = formData.getFirst(AuthenticatorConstants.USER_PASSWORD_CONFIRM); //TODO : ftl 받으면 살리자

//                if (StringUtil.isEmpty(password) || StringUtil.isEmpty(confirmPassword)) {
                if (StringUtil.isEmpty(password) ) {
                    Response challenge =  context.form()
                            .setError("Empty Password").createForm("password-update-alert-error.ftl");
                    context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
                } else if (!Pattern.compile("^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[$@$!%*?&])|(?=.*[a-z])(?=.*\\d)(?=.*[$@$!%*?&])|(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&]))[A-Za-z\\d$@$!%*?&]{9,20}$")
                        .matcher(password).matches()) {
                    Response challenge =  context.form()
                            .setError("Invalid password: violate the password policy").createForm("password-update-alert-error.ftl");
                    context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
                }
//                else if (!password.equalsIgnoreCase(confirmPassword)){
//                    Response challenge =  context.form()
//                            .setError("Password and confirmation does not match. ").createForm("security-policy-validation-error.ftl");
//                    context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
//                }

                // Change Password
                System.out.println("User [ " + context.getUser().getUsername() + " ] Change Password to " + password);
                context.getSession().userCredentialManager().updateCredential(context.getRealm(),
                        context.getUser(),
                        UserCredentialModel.password(password, false));
                System.out.println("User [ " + context.getUser().getUsername() + " ] Change Password Success");

                // Event Publish
                EventBuilder event = new EventBuilder(context.getRealm(), context.getSession(), context.getConnection());
                event.event(EventType.UPDATE_PASSWORD).user(context.getUser()).realm("tmax").detail("username", context.getUser().getUsername()).success();
                context.success();
            }
        }
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
