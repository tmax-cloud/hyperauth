package com.tmax.hyperauth.authenticator.outer2ndFactor;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import com.tmax.hyperauth.caller.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class Outer2ndFactorAuthenticator implements Authenticator {
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        log.info("user [ "+ context.getUser().getUsername() + " ] Outer 2nd Factor Authenticator Start");
        try{
            AuthenticatorConfigModel config = context.getAuthenticatorConfig();
            UserModel user = context.getUser();
            RealmModel realm = context.getRealm();
            String tabId = context.getAuthenticationSession().getTabId();
            log.info("tabID : " + tabId);
            long ttl = AuthenticatorUtil.getConfigLong(config, AuthenticatorConstants.CONF_PRP_WAIT_SEC, 5 * 60L); // 5 minutes in s
            Long expiringAt = new Date().getTime() + (ttl * 1000);

            // Validate Authenticator
            validateAuthenticator( context );

            // tabID, ExpiringAt 1차 인증을 거친 유저가 2차 인증을 기다리고 있다는 표시를 해야한다. (User Credential)
            storeUserOuterAuthInfo(context, tabId, expiringAt); // s --> ms

            //userName, realmName, tabID, phone number, url, expiringAt Attribute으로 넘겨주자
            Response challenge = context.form()
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_USER_NAME, user.getUsername())
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_REALM_NAME, realm.getName())
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_TAB_ID, tabId)
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_ADDI_PARAM,
                            AuthenticatorUtil.getAttributeValue(context.getUser(),
                                    AuthenticatorUtil.getConfigString(config, AuthenticatorConstants.CONF_PRP_ADDI_PARAM)))
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OUTER_URL,
                            AuthenticatorUtil.getConfigString(config,AuthenticatorConstants.CONF_PRP_OUTER_URL ))
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OUTER_EXP_AT, expiringAt.toString())
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OUTER_WAIT_TTL, Long.valueOf(ttl).intValue())
                    .createForm("outer-2nd-factor-wait.ftl");
            context.challenge(challenge);
        } catch(Exception e){
            log.error("Error Occurred", e);
            Response challenge = context.form()
                    .setError(e.getMessage())
                    .createForm("outer-2nd-factor-wait-error.ftl");
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, challenge);
        }
    }


    @Override
    public void action(AuthenticationFlowContext context) {
        //Check if User Credential 2nd-factor.auth-status is "on"
        String outer2ndFactorStatus = context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(),
                AuthenticatorConstants.USR_CRED_OUTER_STATUS).get(0).getCredentialData();
        if (outer2ndFactorStatus.equalsIgnoreCase("on")){
            log.info("Outer 2nd Factor Authentication Passed, User [ " + context.getUser().getUsername() + " ]");
            context.success();
        } else {
            log.info("Outer 2nd Factor Authentication Not Passed Yet, Redirect to Waiting Page User [ " + context.getUser().getUsername() + " ]");

            AuthenticatorConfigModel config = context.getAuthenticatorConfig();
            UserModel user = context.getUser();
            RealmModel realm = context.getRealm();
            String tabId = context.getAuthenticationSession().getTabId();
            long ttl = AuthenticatorUtil.getConfigLong(config, AuthenticatorConstants.CONF_PRP_WAIT_SEC, 5 * 60L); // 5 minutes in s
            String expTimeString = context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(),
                    AuthenticatorConstants.USR_CRED_OUTER_WAIT_EXP_TIME).get(0).getCredentialData();
            log.info("Outer 2nd Factor Authentication TTL time : " + Long.valueOf(ttl).intValue() + " second");
            Response challenge = context.form()
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_USER_NAME, user.getUsername())
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_REALM_NAME, realm.getName())
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_TAB_ID, tabId)
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_ADDI_PARAM,
                            AuthenticatorUtil.getAttributeValue(context.getUser(),
                                    AuthenticatorUtil.getConfigString(config, AuthenticatorConstants.CONF_PRP_ADDI_PARAM)))
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OUTER_URL,
                            AuthenticatorUtil.getConfigString(config,AuthenticatorConstants.CONF_PRP_OUTER_URL ))
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OUTER_EXP_AT, expTimeString)
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OUTER_WAIT_TTL, Long.valueOf(ttl).intValue())
                    .createForm("outer-2nd-factor-wait.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
        }
    }

    private void storeUserOuterAuthInfo(AuthenticationFlowContext context, String tabId, Long expiringAt) {
        // For outer-2nd-factor Tab ID
        CredentialModel credentials = new CredentialModel();
        credentials.setId(UUID.randomUUID().toString());
        credentials.setCreatedDate(new Date().getTime());
        credentials.setType(AuthenticatorConstants.USR_CRED_OUTER_TAB_ID);
        credentials.setCredentialData(tabId);

        // Delete Previous Credentials if Exists
        List< CredentialModel > storedCredentials = context.getSession().userCredentialManager()
                .getStoredCredentialsByType(context.getRealm(), context.getUser(), AuthenticatorConstants.USR_CRED_OUTER_TAB_ID);
        removeCredentials(context, storedCredentials);

        // Create New Credentials
        context.getSession().userCredentialManager().createCredential(context.getRealm(), context.getUser(), credentials);


        // For outer-2nd-factor Exp-Time
        credentials.setId(UUID.randomUUID().toString());
        credentials.setCreatedDate(new Date().getTime());
        credentials.setType(AuthenticatorConstants.USR_CRED_OUTER_WAIT_EXP_TIME);
        credentials.setCredentialData(expiringAt.toString());

        // Delete Previous Credentials if Exists
        storedCredentials = context.getSession().userCredentialManager()
                .getStoredCredentialsByType(context.getRealm(), context.getUser(), AuthenticatorConstants.USR_CRED_OUTER_WAIT_EXP_TIME);
        removeCredentials(context, storedCredentials);

        // Create New Credentials
        context.getSession().userCredentialManager().createCredential(context.getRealm(), context.getUser(), credentials);

        // For outer-2nd-factor Auth Status
        credentials.setId(UUID.randomUUID().toString());
        credentials.setCreatedDate(new Date().getTime());
        credentials.setType(AuthenticatorConstants.USR_CRED_OUTER_STATUS);
        credentials.setCredentialData("off");

        // Delete Previous Credentials if Exists
        storedCredentials = context.getSession().userCredentialManager()
                .getStoredCredentialsByType(context.getRealm(), context.getUser(), AuthenticatorConstants.USR_CRED_OUTER_STATUS);
        removeCredentials(context, storedCredentials);

        // Create New Credentials
        context.getSession().userCredentialManager().createCredential(context.getRealm(), context.getUser(), credentials);
    }

    private void validateAuthenticator( AuthenticationFlowContext context) {
        if (StringUtil.isEmpty(AuthenticatorUtil.getConfigString(context.getAuthenticatorConfig(), AuthenticatorConstants.CONF_PRP_ADDI_PARAM))){
            log.error(AuthenticatorConstants.CONF_PRP_ADDI_PARAM + " is not set in Authenticator Config");
            Response challenge = context.form()
                    .setError(AuthenticatorConstants.CONF_PRP_ADDI_PARAM + " is not set in Authenticator Config" )
                    .createForm("outer-2nd-factor-wait-error.ftl");
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, challenge);

        } else if (StringUtil.isEmpty(AuthenticatorUtil.getAttributeValue(context.getUser(),
                AuthenticatorUtil.getConfigString(context.getAuthenticatorConfig(), AuthenticatorConstants.CONF_PRP_ADDI_PARAM)))){
            log.error(AuthenticatorConstants.CONF_PRP_ADDI_PARAM + " is not set in user [ "+ context.getUser().getUsername() + " ] Attributes");
            Response challenge = context.form()
                    .setError(AuthenticatorConstants.CONF_PRP_ADDI_PARAM + " is not set in user [ "+ context.getUser().getUsername() + " ] Attributes" )
                    .createForm("outer-2nd-factor-wait-error.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, challenge);

        }

        if (StringUtil.isEmpty(AuthenticatorUtil.getConfigString(context.getAuthenticatorConfig(), AuthenticatorConstants.CONF_PRP_OUTER_URL))){
            log.error(AuthenticatorConstants.CONF_PRP_OUTER_URL + " is not set in Authenticator Config");
            Response challenge = context.form()
                    .setError(AuthenticatorConstants.CONF_PRP_OUTER_URL + " is not set in Authenticator Config" )
                    .createForm("outer-2nd-factor-wait-error.ftl");
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, challenge);
        }
    }

    private void removeCredentials(AuthenticationFlowContext context, List< CredentialModel > storedCredentials) {
        if ( storedCredentials != null && storedCredentials.size() > 0) {
            for ( CredentialModel storedCredential : storedCredentials) {
                context.getSession().userCredentialManager().removeStoredCredential(context.getRealm(), context.getUser(), storedCredential.getId());
            }
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
