package com.tmax.hyperauth.authenticator.securityPolicy;

import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class SecurityPolicyAuthenticator implements Authenticator {

    protected boolean isSecurityPolicyEnabled(AuthenticationFlowContext context) {
        boolean flag = false;
        String isIpBLock = AuthenticatorUtil.getAttributeValue(context.getUser(), "ipBlock");
        log.info("isSecurityPolicyEnabled From User Attribute : " + isIpBLock + ", user [ "+ context.getUser().getUsername() + " ]");
        if (isIpBLock != null && isIpBLock.equalsIgnoreCase("true")){
            flag = true;
        }
        return flag;
    }

    protected boolean isSecurityPolicyPassed(AuthenticationFlowContext context) {
        log.info("User [ "+ context.getUser().getUsername() + " ] Login from IP Address : " + context.getConnection().getRemoteAddr());
        List< String > ipPermitList = context.getUser().getAttribute("ipPermitList");
        if ( ipPermitList != null ){
            for (String ipPermit : ipPermitList) {
                log.info("ipPermit From User Attribute : " + ipPermit + ", user [ "+ context.getUser().getUsername() + " ]");
                SubnetUtils utils = null;
                try{
                    utils = new SubnetUtils(ipPermit);
                    utils.setInclusiveHostCount(true);
                }catch(IllegalArgumentException e) {
                    log.error("Error Occurs!!", e);
                    log.info("Invalid CIDR syntax : " + ipPermit);
                    return false;
                }
                for ( String address :  utils.getInfo().getAllAddresses()){
                    log.debug(address);
                }
                if (utils.getInfo().isInRange(context.getConnection().getRemoteAddr())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (!isSecurityPolicyEnabled(context) ) {
            log.info("Bypassing Security Policy since disabled user [ " + context.getUser().getUsername() +" ]");
            context.success();
            return;
        } else if ( isSecurityPolicyPassed(context)) {
            log.info("Security Policy Passed!!, user [ "+ context.getUser().getUsername() + " ]");
            context.success();
            return;

        }else{
            log.info("Blocked by Security Policy!! , User[ " + context.getUser().getUsername() +" ]");
            Response challenge =  context.form()
                    .setError("Blocked by Security Policy.").createForm("security-policy-validation-error.ftl");
            context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) { context.success(); }


    @Override
    public boolean requiresUser() { return true; }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) { return true; }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) { }

    @Override
    public void close() { }
}
