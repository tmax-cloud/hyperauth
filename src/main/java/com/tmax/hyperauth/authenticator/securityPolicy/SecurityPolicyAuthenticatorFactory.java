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

    public static final String PROVIDER_ID = "security-policy-authenticator ";
    private static final SecurityPolicyAuthenticator SINGLETON = new SecurityPolicyAuthenticator();

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

//    static {
//        ProviderConfigProperty property;
//
//        property = new ProviderConfigProperty();
//        property.setName("security-policy.enabled");
//        property.setLabel("Ip block Enabled");
//        property.setType(ProviderConfigProperty.BOOLEAN_TYPE);
//        property.setHelpText("Is Ip block Enabled");
//        configProperties.add(property);
//
//        property = new ProviderConfigProperty();
//        property.setName("security-policy.whitelist");
//        property.setLabel("Ip block white list");
//        property.setType(ProviderConfigProperty.MULTIVALUED_STRING_TYPE);
//        property.setHelpText("Ip block white list");
//        configProperties.add(property);
//    }

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
//        System.out.println("isConfigurable called ... returning true");
        return true;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }




    @Override
    public String getHelpText() {
//        System.out.println("getHelpText called ...");
        return "Ip Block based on User Security White List";
    }

    @Override
    public String getDisplayType() {
//        System.out.println("getDisplayType called ... returning User Security Policy");
        return "User Security Policy";
    }

    @Override
    public String getReferenceCategory() {
//        System.out.println("getReferenceCategory called ... returning user-security-policy");
        return "user-security-policy";
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

