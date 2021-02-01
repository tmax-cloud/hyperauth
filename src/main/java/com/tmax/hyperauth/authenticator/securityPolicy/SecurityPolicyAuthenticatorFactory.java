package com.tmax.hyperauth.authenticator.securityPolicy;

import org.jboss.logging.Logger;
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
public class SecurityPolicyAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "security-policy-authenticator";
    private static final SecurityPolicyAuthenticator SINGLETON = new SecurityPolicyAuthenticator();

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

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
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getHelpText() { return "Ip Block based on User Security White List"; }

    @Override
    public String getDisplayType() { return "User Security Policy"; }

    @Override
    public String getReferenceCategory() { return "user-security-policy"; }

    @Override
    public void init(Config.Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }
}

