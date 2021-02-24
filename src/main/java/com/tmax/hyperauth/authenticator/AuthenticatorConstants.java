package com.tmax.hyperauth.authenticator;

public class AuthenticatorConstants {

    //// OTP
    public static final String ANSW_OTP_CODE = "otpCode";

    // Configurable fields
    public static final String CONF_PRP_OTP_CODE_TTL = "email.otp.code.ttl";
    public static final String CONF_PRP_OTP_CODE_LENGTH = "email.otp.code.length";
    public static final String CONF_PRP_OTP_TEXT = "email.otp.msg.text";
    public static final String ATTR_PRP_OTP_EXP_AT = "emailOtpCodeExpAt";
    public static final String ATTR_PRP_OTP_CODE_TTL = "emailOtpCodeTtl";

    // User credentials (used to persist the sent otp code + expiration time cluster wide)
    public static final String USR_CRED_MDL_OTP_CODE = "email-otp.code";
    public static final String USR_CRED_MDL_OTP_EXP_TIME = "email-otp.exp-time";

    //// PasswordUpdateAlert
    public static final String CONF_PW_UPDATE_PERIOD = "password.update.period";
    public static final String CONF_PW_UPDATE_SKIP_PERIOD = "password.update.skip.period";

    public static final String USER_ATTR_LAST_PW_UPDATE_DATE = "lastPasswordUpdateTime";

    public static final String PW_UPDATE_SKIP = "passwordUpdateSkip";
    public static final String USER_PASSWORD = "password";
    public static final String USER_PASSWORD_CONFIRM = "confirmPassword";

    //// Withdrawal Cancel
    public static final String USER_ATTR_DELETION_DATE = "deletionDate";

    //// Tmax Password Policy
    public static final String TMAX_REALM_PASSWORD_POLICY = "^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()<>?])|(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()<>?])|(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()<>?]))[A-Za-z\\d!@#$%^&*()<>?]{9,20}$";

}
