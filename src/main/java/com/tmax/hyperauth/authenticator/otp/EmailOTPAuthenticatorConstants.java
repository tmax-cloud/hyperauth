package com.tmax.hyperauth.authenticator.otp;

import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.UserModel;

import java.util.List;

public class EmailOTPAuthenticatorConstants {
    public static final String ANSW_OTP_CODE = "otpCode";

    // Configurable fields
    public static final String CONF_PRP_OTP_CODE_TTL = "email-otp.code.ttl";
    public static final String CONF_PRP_OTP_CODE_LENGTH = "email-otp.code.length";
    public static final String CONF_PRP_OTP_TEXT = "email-otp.msg.text";

    // User credentials (used to persist the sent otp code + expiration time cluster wide)
    public static final String USR_CRED_MDL_OTP_CODE = "email-otp.code";
    public static final String USR_CRED_MDL_OTP_EXP_TIME = "email-otp.exp-time";
}
