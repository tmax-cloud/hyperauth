package com.tmax.hyperauth.authenticator.securityPolicy;

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
public class SecurityPolicyAuthenticator implements Authenticator {

    protected boolean isSecurityPolicyEnabled(AuthenticationFlowContext context) {
        boolean flag = false;
        String isIpBLock = SecurityPolicyAuthenticatorUtil.getAttributeValue(context.getUser(), "ipBlock");
        System.out.println("isSecurityPolicyEnabled From Attribute : " + isIpBLock);
        if (isIpBLock != null && isIpBLock.equalsIgnoreCase("true")){
            flag = true;
        }
        return flag;
    }

    protected boolean isSecurityPolicyPassed(AuthenticationFlowContext context) {
        List< String > ipPermitList = context.getUser().getAttribute("ipPermitList");
        if ( ipPermitList != null ){
            for (String ipPermit : ipPermitList) {
                System.out.println("ipPermit From Attribute : " + ipPermit);
                SubnetUtils utils = null;
                try{
                    utils = new SubnetUtils(ipPermit);
                }catch(IllegalArgumentException e) {
                    System.out.println("Invalid CIDR syntax : " + ipPermit);
                    return false;
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
//        System.out.println("authenticate called ... User = " + context.getUser().getUsername());

        if (!isSecurityPolicyEnabled(context) ) {
            System.out.println("Bypassing Security Policy since disabled user [ " + context.getUser().getUsername() +" ]");
            context.success();
//            context.challenge(context.form().createForm("test.ftl"));
            return;
        } else if ( !isSecurityPolicyPassed(context)) {
            System.out.println("Security Policy Passed!!, user [ "+ context.getUser().getUsername() + " ]");
            context.success();
            return;

        }else{
            Response challenge =  context.form()
                    .setError("Blocked by Security Policy.").createForm("security-policy-validation-error.ftl");
            context.failureChallenge(AuthenticationFlowError.USER_DISABLED, challenge);
            System.out.println("Blocked by Security Policy!! , User[ " + context.getUser().getUsername() +" ]");

        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
//        System.out.println("action called ... context = " + context);
        context.success();
    }


    @Override
    public boolean requiresUser() {
//        System.out.println("requiresUser called ... returning true");
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
//        System.out.println("configuredFor called ... session=" + session + ", realm=" + realm + ", user=" + user);
//        System.out.println("... returning true");
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
//        System.out.println("setRequiredActions called ... session=" + session + ", realm=" + realm + ", user=" + user);
    }

    @Override
    public void close() {
//        System.out.println("close called ...");
    }
}
