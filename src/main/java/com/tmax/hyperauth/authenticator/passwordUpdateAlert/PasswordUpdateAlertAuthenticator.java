package com.tmax.hyperauth.authenticator.passwordUpdateAlert;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import com.tmax.hyperauth.caller.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.*;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

/**
 * @author taegeon_woo@tmax.co.krt
 */

@Slf4j
public class PasswordUpdateAlertAuthenticator implements Authenticator {

    protected boolean hasTimePassed(AuthenticationFlowContext context, int period) {
        try{
            long lastPWUpdateTime = (AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_LAST_PW_UPDATE_DATE) != null)?
                    Long.valueOf(AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_LAST_PW_UPDATE_DATE)) : context.getUser().getCreatedTimestamp();
            long now = System.currentTimeMillis();
            long monthLong = 1000L * 60 * 60 * 24 * 30;  //2,592,000,000   2,794,503,078
            log.debug( "now : " + now);
            log.debug( "lastPWUpdateTime : " + lastPWUpdateTime);
            log.debug( "now-lastPWUpdateTime : " + (now-lastPWUpdateTime));

            if ((now-lastPWUpdateTime) > monthLong * period){
                log.debug( "return true");
                return true;
            }
        }catch(Exception e){
            log.error("Error Occurs!!", e);
        }
        return false;
    }

    private boolean isUserHasPasswordCredential(AuthenticationFlowContext context) {
        if( context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(), "password")!= null
                && context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(), "password").size() >0
                && context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(), "password") != null ) {
            return true;
        }
        return false;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        log.debug("authenticate called ... User = " + context.getUser().getUsername());
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int period = AuthenticatorUtil.getConfigInt(config, AuthenticatorConstants.CONF_PW_UPDATE_PERIOD, 3);
        log.info("Using Password Update Period  : " + period + ", user [ "+ context.getUser().getUsername() + " ]");

        if (!isUserHasPasswordCredential(context)){
            log.info("User [ " + context.getUser().getUsername() + " ] Has no Password Credential in Hyperauth, Do Not Need to Update Password");
            context.success();
            return;
        }

        if (!hasTimePassed(context, period) ) {
            log.info("User [ " + context.getUser().getUsername() + " ] Do Not Need to Update Password");
            context.success();
            return;
        } else {
            log.info("User [ " + context.getUser().getUsername() + " ] Need to Update Password");
            Response challenge = context.form().createForm("password-update-alert-choose.ftl");
            context.challenge(challenge);
        }
    }



    @Override
    public void action(AuthenticationFlowContext context) {
        log.debug("action called ... context = " + context);

        if (context.getHttpRequest().getDecodedFormParameters() != null) {
            MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
            String skip = formData.getFirst(AuthenticatorConstants.PW_UPDATE_SKIP);

            if (skip != null && skip.equalsIgnoreCase("t")) {
                // 다음에 변경하기 클릭시
                log.info("User [ " + context.getUser().getUsername() + " ] Skip Password Update !");
                log.info("User [ " + context.getUser().getUsername() + " ] Ask Again Later");
                context.success();

            } else if (skip != null && skip.equalsIgnoreCase("f")){
                // 변경하기 클릭시, 비밀번호 변경 페이지를 던진다.
                Response challenge = context.form().createForm("password-update-alert.ftl");
                context.challenge(challenge);

            } else {
                // 비밀번호 변경 페이지
                log.info("User [ " + context.getUser().getUsername() + " ] Insert New Password !");
                String password = formData.getFirst(AuthenticatorConstants.USER_PASSWORD);
                String confirmPassword = formData.getFirst(AuthenticatorConstants.USER_PASSWORD_CONFIRM);

                if (StringUtil.isEmpty(password) || StringUtil.isEmpty(confirmPassword)) {
                    Response challenge = context.form()
                            .setError("Empty Password").createForm("password-update-alert.ftl");
                    context.failureChallenge(AuthenticationFlowError.CREDENTIAL_SETUP_REQUIRED, challenge);
                } else if (!Pattern.compile(AuthenticatorConstants.TMAX_REALM_PASSWORD_POLICY)
                        .matcher(password).matches()) {
                    Response challenge = context.form()
                            .setError("Invalid password: violate the password policy").createForm("password-update-alert.ftl");
                    context.failureChallenge(AuthenticationFlowError.CREDENTIAL_SETUP_REQUIRED, challenge);
                } else if (!password.equalsIgnoreCase(confirmPassword)) {
                    Response challenge = context.form()
                            .setError("Password and confirmation does not match.").createForm("password-update-alert.ftl");
                    context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
                }
                // Verfify if Same with Old password
                else if (sameWithOldPW(password, context)) {
                    Response challenge = context.form()
                            .setError("sameWithOldPassword").createForm("password-update-alert.ftl");
                    context.failureChallenge(AuthenticationFlowError.CREDENTIAL_SETUP_REQUIRED, challenge);
                } else {
                    // Change Password
//                    log.debug("User [ " + context.getUser().getUsername() + " ] Change Password to " + password);
                    context.getSession().userCredentialManager().updateCredential(context.getRealm(),
                            context.getUser(),
                            UserCredentialModel.password(password, false));
                    log.info("User [ " + context.getUser().getUsername() + " ] Change Password Success");

                    // Event Publish
                    EventBuilder event = new EventBuilder(context.getRealm(), context.getSession(), context.getConnection());
                    event.event(EventType.UPDATE_PASSWORD).user(context.getUser()).realm(context.getSession().getContext().getRealm())
                            .detail("username", context.getUser().getUsername()).success();
                    context.success();
                }
            }
        }
    }

    private boolean sameWithOldPW(String password, AuthenticationFlowContext context) {
        UserCredentialModel cred = UserCredentialModel.password(password);
        if (context.getSession().userCredentialManager().isValid(context.getRealm(), context.getUser(), cred)) {
            return true;
        } else {
            return false;
        }
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
