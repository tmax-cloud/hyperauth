package com.tmax.hyperauth.authenticator.withdrawalCancel;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import com.tmax.hyperauth.rest.Util;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author taegeon_woo@tmax.co.krt
 */
public class WithdrawalCancelAuthenticator implements Authenticator {

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        System.out.println("authenticate called ... User = " + context.getUser().getUsername());
        if (AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_DELETION_DATE) != null) {
            System.out.println("User [ " + context.getUser().getUsername() + " ] Need to Withdrawal Cancel to Login");
            Response challenge = context.form().setAttribute(AuthenticatorConstants.USER_ATTR_DELETION_DATE,
                    AuthenticatorUtil.getAttributeValue(context.getUser(), AuthenticatorConstants.USER_ATTR_DELETION_DATE)).createForm("withdrawal-cancel.ftl");
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

        // Send Mail
        String subject = "[Tmax 통합계정] 탈퇴 신청이 취소되었습니다.";
        String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/account-withdrawal-cancel.html");
        List<Util.MailImage> imageParts = new ArrayList<>(
                Arrays.asList(
                        new Util.MailImage( "/opt/jboss/keycloak/themes/tmax/email/html/resources/img/secession_cancel.svg","secession_cancel" )
                )
        );        try {
            Util.sendMail(context.getSession(), context.getUser().getEmail(), subject, body, imageParts );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception " + e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("Exception " + throwable.getMessage());
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

