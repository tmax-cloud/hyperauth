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
    public static final String USR_CRED_OUTER_TAB_ID = "2nd-factor.tabId";
    public static final String USR_CRED_OUTER_WAIT_EXP_TIME = "2nd-factor.exp-time";
    public static final String USR_CRED_OUTER_STATUS = "2nd-factor.auth-status";

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

    //// General 2nd Factor
    public static final String CONF_PRP_OUTER_2ND_FACTOR_ALIAS = "outer2ndFactor";
    public static final String CONF_PRP_WAIT_SEC = "general.2nd.wait.sec";
    public static final String CONF_PRP_ADDI_PARAM = "general.2nd.additional.parameter";
    public static final String CONF_PRP_OUTER_URL = "general.2nd.outer.url";
    public static final String CONF_PRP_SECRET_KEY = "general.2nd.secret.key";
    public static final String ATTR_PRP_USER_NAME = "userName";
    public static final String ATTR_PRP_REALM_NAME = "realmName";
    public static final String ATTR_PRP_TAB_ID = "tabId";
    public static final String ATTR_PRP_ADDI_PARAM = "additionalParameter";
    public static final String ATTR_PRP_SECRET_KEY = "secretKey";
    public static final String ATTR_PRP_OUTER_URL = "outerUrl";
    public static final String ATTR_PRP_OUTER_WAIT_TTL = "outerAuthWaitTtl";
    public static final String ATTR_PRP_OUTER_EXP_AT = "outerAuthExpAt";
}
