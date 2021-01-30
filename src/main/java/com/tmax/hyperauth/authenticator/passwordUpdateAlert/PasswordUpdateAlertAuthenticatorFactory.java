package com.tmax.hyperauth.authenticator.passwordUpdateAlert;

import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.authenticator.securityPolicy.SecurityPolicyAuthenticator;
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
public class PasswordUpdateAlertAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "password-update-alert-authenticator ";
    private static final PasswordUpdateAlertAuthenticator SINGLETON = new PasswordUpdateAlertAuthenticator();

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PW_UPDATE_PERIOD);
        property.setLabel("Password Update Period in Month");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("User will be asked to change password after certain month since last password update Date");
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(AuthenticatorConstants.CONF_PW_UPDATE_SKIP_PERIOD);
        property.setLabel("Password Update Skip Period in Month");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Password Update Will be asked certain Month later, Shold Less than Password Update Period");
        configProperties.add(property);
    }

    @Override
    public String getId() {
        System.out.println("getId called ... returning " + PROVIDER_ID);
        return PROVIDER_ID;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        System.out.println("create called ... returning " + SINGLETON);
        return SINGLETON;
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        System.out.println("getRequirementChoices called ... returning " + REQUIREMENT_CHOICES);
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        System.out.println("isUserSetupAllowed called ... returning true");
        return true;
    }

    @Override
    public boolean isConfigurable() {
        System.out.println("isConfigurable called ... returning true");
        return true;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }




    @Override
    public String getHelpText() {
        System.out.println("getHelpText called ...");
        return "Password Update Alert When User Login After Certain Month Since User Update Password";
    }

    @Override
    public String getDisplayType() {
        System.out.println("getDisplayType called ... returning User Security Policy");
        return "Password Update Alert";
    }

    @Override
    public String getReferenceCategory() {
        System.out.println("getReferenceCategory called ... returning user-security-policy");
        return "password-update-alert";
    }

    @Override
    public void init(Config.Scope config) {
        System.out.println("init called ... config.scope = " + config);
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        System.out.println("postInit called ... factory = " + factory);
    }

    @Override
    public void close() {
        System.out.println("close called ...");
    }
}