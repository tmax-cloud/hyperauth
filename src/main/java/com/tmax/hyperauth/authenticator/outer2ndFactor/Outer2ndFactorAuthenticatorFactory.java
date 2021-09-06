package com.tmax.hyperauth.authenticator.outer2ndFactor;

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
public class Outer2ndFactorAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "General-2nd-factor-authenticator";
    private static final Outer2ndFactorAuthenticator SINGLETON = new Outer2ndFactorAuthenticator();

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
        // TIME TO WAIT
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_WAIT_SEC);
        property.setLabel("time to wait in sec");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("time to wait for outer 2nd factor authenticator in seconds.");
        configProperties.add(property);

        // ADDTITIONAL PARAMETER
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_ADDI_PARAM);
        property.setLabel("Additional parameter to send");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Additional parameter to send in user attribute.");
        configProperties.add(property);

        // OUTER URL
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_OUTER_URL);
        property.setLabel("Outer Url");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Outer Url of 2nd factor authenticator");
        configProperties.add(property);

        // SECRET KEY
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PRP_SECRET_KEY);
        property.setLabel("Secret Key");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Secret Key to share with Outer Authenticator");
        configProperties.add(property);
    }

    @Override
    public String getHelpText() { return "General outer 2nd factor Authenticator"; }

    @Override
    public String getDisplayType() { return "Outer 2nd Factor"; }

    @Override
    public String getReferenceCategory() { return "outer-2nd-factor"; }

    @Override
    public void init(Config.Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }
}

