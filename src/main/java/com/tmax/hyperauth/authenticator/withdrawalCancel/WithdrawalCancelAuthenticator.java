package com.tmax.hyperauth.authenticator.withdrawalCancel;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import com.tmax.hyperauth.rest.Util;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;
import javax.ws.rs.core.Response;


/**
 * @author taegeon_woo@tmax.co.krt
 */

@Slf4j
public class WithdrawalCancelAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_DELETION_DATE) != null) {
            log.info("User [ " + context.getUser().getUsername() + " ] Need to Withdrawal Cancel to Login");
            Response challenge = context.form().setAttribute(AuthenticatorConstants.USER_ATTR_DELETION_DATE,
                    AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_DELETION_DATE)).createForm("withdrawal-cancel.ftl");
            context.challenge(challenge);
        } else {
            log.info("User [ " + context.getUser().getUsername() + " ] Do Not Need to Withdrawal Check");
            context.success();
            return;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        log.info("User [ " + context.getUser().getUsername() + " ] Cancelled withdrawal!!");
        context.getUser().removeAttribute(AuthenticatorConstants.USER_ATTR_DELETION_DATE);

        // Send Mail
        String subject = "[Tmax 통합계정] 탈퇴 신청이 취소되었습니다.";
        String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/account-withdrawal-cancel.html");
        String emailTheme = context.getSession().realms().getRealmByName(context.getRealm().getName()).getEmailTheme();
        if(!emailTheme.equalsIgnoreCase("tmax") && !emailTheme.equalsIgnoreCase("base") && !emailTheme.equalsIgnoreCase("keycloak")) {
            subject = "[" + emailTheme + "] 탈퇴 신청이 취소되었습니다.";
            body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/" + emailTheme + "/email/html/etc/account-withdrawal-cancel.html");
        }

        try {
            Util.sendMail(context.getSession(), context.getUser().getEmail(), subject, body, null, context.getRealm().getId() );
        } catch (Exception e) {
            log.error("Error Occurs!!", e);
        } catch (Throwable throwable) {
            log.error("Error Occurs!!", throwable);
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

