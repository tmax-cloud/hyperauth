package com.tmax.hyperauth.authenticator.securityPolicy;

import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
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
        String isIpBLock = AuthenticatorUtil.getAttributeValue(context.getUser(), "ipBlock");
        System.out.println("isSecurityPolicyEnabled From User Attribute : " + isIpBLock + ", user [ "+ context.getUser().getUsername() + " ]");
        if (isIpBLock != null && isIpBLock.equalsIgnoreCase("true")){
            flag = true;
        }
        return flag;
    }

    protected boolean isSecurityPolicyPassed(AuthenticationFlowContext context) {
        System.out.println("User [ "+ context.getUser().getUsername() + " ] Login from IP Address : " + context.getConnection().getRemoteAddr());
        List< String > ipPermitList = context.getUser().getAttribute("ipPermitList");
        if ( ipPermitList != null ){
            for (String ipPermit : ipPermitList) {
                System.out.println("ipPermit From User Attribute : " + ipPermit + ", user [ "+ context.getUser().getUsername() + " ]");
                SubnetUtils utils = null;
                try{
                    utils = new SubnetUtils(ipPermit);
                    utils.setInclusiveHostCount(true);
                }catch(IllegalArgumentException e) {
                    System.out.println("Invalid CIDR syntax : " + ipPermit);
                    return false;
                }
                // for test
//                for ( String address :  utils.getInfo().getAllAddresses()){
//                    System.out.println(address);
//                }
                // for test
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
            System.out.println("Bypassing Security Policy since disabled user [ " + context.getUser().getUsername() +" ]");
            context.success();
            return;
        } else if ( isSecurityPolicyPassed(context)) {
            System.out.println("Security Policy Passed!!, user [ "+ context.getUser().getUsername() + " ]");
            context.success();
            return;

        }else{
            System.out.println("Blocked by Security Policy!! , User[ " + context.getUser().getUsername() +" ]");
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
