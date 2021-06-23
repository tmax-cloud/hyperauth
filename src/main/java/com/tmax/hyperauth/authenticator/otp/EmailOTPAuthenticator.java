package com.tmax.hyperauth.authenticator.otp;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.AuthenticatorUtil;
import com.tmax.hyperauth.rest.Util;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * @author taegeon_woo@tmax.co.kr
 */

@Slf4j
public class EmailOTPAuthenticator implements Authenticator {

    private enum CODE_STATUS {
        VALID,
        INVALID,
        EXPIRED
    }

    protected boolean isOTPEnabled(AuthenticationFlowContext context) {
        boolean flag = false;
        String otpEnabled = AuthenticatorUtil.getAttributeValue(context.getUser(), "otpEnable");
        log.info("otpEnabled From Attribute : " + otpEnabled + ", user [ "+ context.getUser().getUsername() + " ]");
        if (otpEnabled != null && otpEnabled.equalsIgnoreCase("true")){
            flag = true;
        }
        return flag;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        // FIXME :  delete
        if(context.getAuthenticationSession().getAuthNote("selection")!= null) {
            log.debug(" context.getAuthenticationSession().getAuthNote(\"selection\") : " + context.getAuthenticationSession().getAuthNote("selection"));
            if (!context.getAuthenticationSession().getAuthNote("selection").equals("mailOtp")){
                context.success();
                return;
            }
        }
        // FIXME :  delete

        if (!isOTPEnabled(context) ) {
            log.info("Bypassing OTP Authenticator since user [ " + context.getUser().getUsername() + " ] has not set OTP Authenticator");
            context.success();
            return;
        }

        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        long nrOfDigits = AuthenticatorUtil.getConfigLong(config, AuthenticatorConstants.CONF_PRP_OTP_CODE_LENGTH, 6L);
        log.info("Using nrOfDigits " + nrOfDigits + ", user [ "+ context.getUser().getUsername() + " ]");

        long ttl = AuthenticatorUtil.getConfigLong(config, AuthenticatorConstants.CONF_PRP_OTP_CODE_TTL, 5 * 60L); // 5 minutes in s
        log.info("Using ttl " + ttl + " (s) , user [ "+ context.getUser().getUsername() + " ]");

        String code = getOTPCode(nrOfDigits);
        log.info("code : " + code + ", user [ "+ context.getUser().getUsername() + " ]");

        Long expiringAt = new Date().getTime() + (ttl * 1000);

        storeOTPInfo(context, code, expiringAt); // s --> ms
        log.info("OTP code Store Success , user [ "+ context.getUser().getUsername() + " ]");

        String subject = "[Tmax 통합계정] 로그인을 위해 인증번호를 입력해주세요.";
        String body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/tmax/email/html/etc/otp-verification-code.html").replaceAll("%%VERIFY_CODE%%", code);

        String emailTheme = context.getSession().realms().getRealmByName(context.getRealm().getName()).getEmailTheme();
        if(!emailTheme.equalsIgnoreCase("tmax") && !emailTheme.equalsIgnoreCase("base") && !emailTheme.equalsIgnoreCase("keycloak")) {
            subject = "[" + emailTheme + "] 로그인을 위해 인증번호를 입력해주세요.";
            body = Util.readLineByLineJava8("/opt/jboss/keycloak/themes/" + emailTheme + "/email/html/etc/otp-verification-code.html").replaceAll("%%VERIFY_CODE%%", code);

        }

        try {
            Util.sendMail(context.getSession(), context.getUser().getEmail(), subject, body, null, context.getRealm().getId());
            Response challenge = context.form()
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OTP_CODE_TTL, Long.valueOf(ttl).intValue())
                    .setAttribute(AuthenticatorConstants.ATTR_PRP_OTP_EXP_AT, (expiringAt).toString())
                    .createForm("email-otp-validation.ftl");
            context.challenge(challenge);

        } catch (Throwable e) {
            log.error("Error Occurs!!", e);
            Response challenge = context.form()
                    .setError("Email OTP could not be sent.")
                    .createForm("email-otp-validation-error.ftl");
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, challenge);
            return;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        long ttl = AuthenticatorUtil.getConfigLong(context.getAuthenticatorConfig(), AuthenticatorConstants.CONF_PRP_OTP_CODE_TTL, 5 * 60L); // 5 minutes in s
        String expTimeString = context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(),
                AuthenticatorConstants.USR_CRED_MDL_OTP_EXP_TIME).get(0).getCredentialData();
        CODE_STATUS status = validateCode(context, expTimeString);
        Response challenge = null;
        switch (status) {
            case EXPIRED:
                challenge =  context.form()
                        .setError("인증번호 유효시간이 만료 되었습니다.")
                        .createForm("email-otp-validation.ftl");
                context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE, challenge);
                break;

            case INVALID:
                if(context.getExecution().getRequirement() == AuthenticationExecutionModel.Requirement.CONDITIONAL || //FIXME
                        context.getExecution().getRequirement() == AuthenticationExecutionModel.Requirement.ALTERNATIVE) {
                    context.attempted();
                } else if(context.getExecution().getRequirement() == AuthenticationExecutionModel.Requirement.REQUIRED) {
                    challenge =  context.form()
                            .setError("인증번호가 틀렸습니다.")
                            .setAttribute(AuthenticatorConstants.ATTR_PRP_OTP_CODE_TTL, Long.valueOf(ttl).intValue())
                            .setAttribute(AuthenticatorConstants.ATTR_PRP_OTP_EXP_AT, expTimeString)
                            .createForm("email-otp-validation.ftl");
                    context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
                } else {
                    // Something strange happened
                    log.info("Undefined execution ...");
                }
                break;

            case VALID:
                context.success();
                break;

        }
    }

    protected CODE_STATUS validateCode(AuthenticationFlowContext context, String expTimeString) {
        CODE_STATUS result = CODE_STATUS.INVALID;
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String enteredCode = formData.getFirst(AuthenticatorConstants.ANSW_OTP_CODE);

        String expectedCode = context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(),
                AuthenticatorConstants.USR_CRED_MDL_OTP_CODE).get(0).getCredentialData();

        log.info("Expected code = " + expectedCode + "    entered code = " + enteredCode + ", user [ "+ context.getUser().getUsername() + " ]");

        if (expectedCode != null) {
            result = enteredCode.equals(expectedCode) ? CODE_STATUS.VALID : CODE_STATUS.INVALID;
            long now = new Date().getTime();

            log.info("Valid code expires in " + (Long.parseLong(expTimeString) - now) + " ms" + ", user [ "+ context.getUser().getUsername() + " ]");
            if (result == CODE_STATUS.VALID) {
                if (Long.parseLong(expTimeString) < now) {
                    log.info("Code is expired !!");
                    result = CODE_STATUS.EXPIRED;
                }
            }
        }
        log.info("validateCode result : " + result);
        return result;
    }

    @Override
    public boolean requiresUser() { return true; }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) { return true; }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) { }

    @Override
    public void close() { }

    private void storeOTPInfo(AuthenticationFlowContext context, String code, Long expiringAt) {
        // For OTP Code
        CredentialModel credentials = new CredentialModel();
        credentials.setId(UUID.randomUUID().toString());
        credentials.setCreatedDate(new Date().getTime());
        credentials.setType(AuthenticatorConstants.USR_CRED_MDL_OTP_CODE);
        credentials.setCredentialData(code);

        // Delete Previous Credentials if Exists
        List< CredentialModel > storedCredentials = context.getSession().userCredentialManager()
                .getStoredCredentialsByType(context.getRealm(), context.getUser(), AuthenticatorConstants.USR_CRED_MDL_OTP_CODE);
        removeCredentials(context, storedCredentials);

        // Create New Credentials
        context.getSession().userCredentialManager().createCredential(context.getRealm(), context.getUser(), credentials);

        // For OTP Code TTL
        credentials.setId(UUID.randomUUID().toString());
        credentials.setCreatedDate(new Date().getTime());
        credentials.setType(AuthenticatorConstants.USR_CRED_MDL_OTP_EXP_TIME);
        credentials.setCredentialData((expiringAt).toString());

        // Delete Previous Credentials if Exists
        storedCredentials = context.getSession().userCredentialManager()
                .getStoredCredentialsByType(context.getRealm(), context.getUser(), AuthenticatorConstants.USR_CRED_MDL_OTP_EXP_TIME);
        removeCredentials(context, storedCredentials);

        // Create New Credentials
        context.getSession().userCredentialManager().createCredential(context.getRealm(), context.getUser(), credentials);
    }

    private String getOTPCode(long nrOfDigits) {
        if(nrOfDigits < 1) {
            throw new RuntimeException("Nr of digits must be bigger than 0");
        }

        double maxValue = Math.pow(10.0, nrOfDigits); // 10 ^ nrOfDigits;
        Random r = new Random();
        long code = (long)(r.nextFloat() * maxValue);
        return Long.toString(code);
    }

    private void removeCredentials(AuthenticationFlowContext context, List< CredentialModel > storedCredentials) {
        if ( storedCredentials != null && storedCredentials.size() > 0) {
            for ( CredentialModel storedCredential : storedCredentials) {
                context.getSession().userCredentialManager().removeStoredCredential(context.getRealm(), context.getUser(), storedCredential.getId());
            }
        }
    }


}
