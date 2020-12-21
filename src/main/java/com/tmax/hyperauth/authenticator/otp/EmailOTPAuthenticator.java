package com.tmax.hyperauth.authenticator.otp;

import com.tmax.hyperauth.caller.Constants;
import com.tmax.hyperauth.rest.Util;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Random;

/**
 * @author taegeon_woo@tmax.co.kr
 */
public class EmailOTPAuthenticator implements Authenticator {

    private static enum CODE_STATUS {
        VALID,
        INVALID,
        EXPIRED
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        System.out.println("authenticate called ... User = " + context.getUser().getUsername());

        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        long nrOfDigits = EmailOTPAuthenticatorUtil.getConfigLong(config, EmailOTPAuthenticatorConstants.CONF_PRP_OTP_CODE_LENGTH, 6L);
        System.out.println("Using nrOfDigits " + nrOfDigits);

        long ttl = EmailOTPAuthenticatorUtil.getConfigLong(config, EmailOTPAuthenticatorConstants.CONF_PRP_OTP_CODE_TTL, 10 * 60L); // 10 minutes in s
        System.out.println("Using ttl " + ttl + " (s)");

        String code = getOTPCode(nrOfDigits);
        System.out.println("code : " + code);

        storeOTPCode(context, code, new Date().getTime() + (ttl * 1000)); // s --> ms
        System.out.println("OTP code Store Success");

        String subject = "[Tmax 통합계정] 로그인을 위해 인증번호를 입력해주세요.";
        String msg = Constants.PASSWORD_VERIFY_CODE_BODY.replaceAll("%%VERIFY_CODE%%", code);
        try {
            Util.sendMail(context.getSession(), context.getUser().getEmail(), subject, msg, null, null);
            Response challenge = context.form().createForm("email-otp-validation.ftl");
            context.challenge(challenge);

        } catch (Throwable e) {
            e.printStackTrace();
            Response challenge = context.form()
                    .setError("Email OTP could not be sent.")
                    .createForm("email-otp-validation-error.ftl");
            context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR, challenge);
            return;
        }
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        System.out.println("action called ... context = " + context);
        CODE_STATUS status = validateCode(context);
        Response challenge = null;
        switch (status) {
            case EXPIRED:
                challenge =  context.form()
                        .setError("code is expired")
                        .createForm("email-otp-validation.ftl");
                context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE, challenge);
                break;

            case INVALID:
                if(context.getExecution().getRequirement() == AuthenticationExecutionModel.Requirement.CONDITIONAL || //FIXME
                        context.getExecution().getRequirement() == AuthenticationExecutionModel.Requirement.ALTERNATIVE) {
                    System.out.println("Calling context.attempted()");
                    context.attempted();
                } else if(context.getExecution().getRequirement() == AuthenticationExecutionModel.Requirement.REQUIRED) {
                    challenge =  context.form()
                            .setError("badCode")
                            .createForm("email-otp-validation.ftl");
                    context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
                } else {
                    // Something strange happened
                    System.out.println("Undefined execution ...");
                }
                break;

            case VALID:
                context.success();
                break;

        }
    }

    protected CODE_STATUS validateCode(AuthenticationFlowContext context) {
        System.out.println("validateCode called ... ");

        CODE_STATUS result = CODE_STATUS.INVALID;
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String enteredCode = formData.getFirst(EmailOTPAuthenticatorConstants.ANSW_OTP_CODE);

        String expectedCode = context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(),
                EmailOTPAuthenticatorConstants.USR_CRED_MDL_OTP_CODE).get(0).getCredentialData();
        String expTimeString = context.getSession().userCredentialManager().getStoredCredentialsByType(context.getRealm(), context.getUser(),
                EmailOTPAuthenticatorConstants.USR_CRED_MDL_OTP_EXP_TIME).get(0).getCredentialData();

        System.out.println("Expected code = " + expectedCode + "    entered code = " + enteredCode);

        if (expectedCode != null) {
            result = enteredCode.equals(expectedCode) ? CODE_STATUS.VALID : CODE_STATUS.INVALID;
            long now = new Date().getTime();

            System.out.println("Valid code expires in " + (Long.parseLong(expTimeString) - now) + " ms");
            if (result == CODE_STATUS.VALID) {
                if (Long.parseLong(expTimeString) < now) {
                    System.out.println("Code is expired !!");
                    result = CODE_STATUS.EXPIRED;
                }
            }
        }
        System.out.println("result : " + result);
        return result;
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

    private void storeOTPCode(AuthenticationFlowContext context, String code, Long expiringAt) {
        UserCredentialModel credentials = new UserCredentialModel();
        credentials.setType(EmailOTPAuthenticatorConstants.USR_CRED_MDL_OTP_CODE);
        credentials.setValue(code);
        context.getSession().userCredentialManager().updateCredential(context.getRealm(), context.getUser(), credentials);
//        context.getSession().users().updateCredential(context.getRealm(), context.getUser(), credentials);

        credentials.setType(EmailOTPAuthenticatorConstants.USR_CRED_MDL_OTP_EXP_TIME);
        credentials.setValue((expiringAt).toString());
        context.getSession().userCredentialManager().updateCredential(context.getRealm(), context.getUser(), credentials);
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


}
