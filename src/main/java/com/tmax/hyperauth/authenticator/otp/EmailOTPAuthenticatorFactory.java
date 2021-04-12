package com.tmax.hyperauth.authenticator.otp;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class EmailOTPAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "email-otp-authenticator";
    private static final EmailOTPAuthenticator SINGLETON = new EmailOTPAuthenticator();

    @Override
    public String getId() { return PROVIDER_ID; }

    @Override
    public Authenticator create(KeycloakSession session) { return SINGLETON; }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() { return REQUIREMENT_CHOICES; }

    @Override
    public boolean isUserSetupAllowed() { return true; }

    @Override
    public boolean isConfigurable() { return true; }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() { return configProperties; }

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        // OTP CODE TTL
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_OTP_CODE_TTL);
        property.setLabel("OTP code time to live");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("The validity of the sent code in seconds.");
        configProperties.add(property);

        // OTP CODE LENGTH
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_OTP_CODE_LENGTH);
        property.setLabel("Length of the OTP code");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Length of the SMS code.");
        configProperties.add(property);

        // OTP Text
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_OTP_TEXT);
        property.setLabel("Template of text to send to the user");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Use %sms-code% as placeholder for the generated SMS code.");
        configProperties.add(property);
    }

    @Override
    public String getHelpText() { return "Email Based OTP Authenticator"; }

    @Override
    public String getDisplayType() { return "Email OTP"; }

    @Override
    public String getReferenceCategory() { return "email-otp"; }

    @Override
    public void init(Config.Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }
}

