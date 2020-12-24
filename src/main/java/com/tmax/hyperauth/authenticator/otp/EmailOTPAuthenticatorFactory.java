package com.tmax.hyperauth.authenticator.otp;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author taegeon_woo@tmax.co.kr
 */
public class EmailOTPAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "email-otp-authenticator ";
    private static final EmailOTPAuthenticator SINGLETON = new EmailOTPAuthenticator();

    @Override
    public String getId() {
//        System.out.println("getId called ... returning " + PROVIDER_ID);
        return PROVIDER_ID;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
//        System.out.println("create called ... returning " + SINGLETON);
        return SINGLETON;
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
//        System.out.println("getRequirementChoices called ... returning " + REQUIREMENT_CHOICES);
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
//        System.out.println("isUserSetupAllowed called ... returning true");
        return true;
    }

    @Override
    public boolean isConfigurable() {
        System.out.println("return EmailOTP Configurable true ");
        return true;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        System.out.println("return EmailOTP ConfigProperties ");
        return configProperties;
    }

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        // OTP CODE TTL
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(EmailOTPAuthenticatorConstants.CONF_PRP_OTP_CODE_TTL);
        property.setLabel("OTP code time to live");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("The validity of the sent code in seconds.");
        configProperties.add(property);

        // OTP CODE LENGTH
        property = new ProviderConfigProperty();
        property.setName(EmailOTPAuthenticatorConstants.CONF_PRP_OTP_CODE_LENGTH);
        property.setLabel("Length of the OTP code");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Length of the SMS code.");
        configProperties.add(property);

        // OTP Text
        property = new ProviderConfigProperty();
        property.setName(EmailOTPAuthenticatorConstants.CONF_PRP_OTP_TEXT);
        property.setLabel("Template of text to send to the user");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Use %sms-code% as placeholder for the generated SMS code.");
        configProperties.add(property);
    }

    @Override
    public String getHelpText() {
//        System.out.println("getHelpText called ...");
        return "Email Based OTP Authenticator";
    }

    @Override
    public String getDisplayType() {
//        System.out.println("getDisplayType called ... returning Email OTP");
        return "Email OTP";
    }

    @Override
    public String getReferenceCategory() {
//        System.out.println("getReferenceCategory called ... returning email-otp");
        return "email-otp";
    }

    @Override
    public void init(Config.Scope config) {
//        System.out.println("init called ... config.scope = " + config);
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
//        System.out.println("postInit called ... factory = " + factory);

    }

    @Override
    public void close() {
//        System.out.println("close called ...");
    }
}

