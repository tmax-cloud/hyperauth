package com.tmax.hyperauth.authenticator.withdrawalCancel;

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
public class WithdrawalCancelAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "withdrawal-cancel-authenticator ";
    private static final WithdrawalCancelAuthenticator SINGLETON = new WithdrawalCancelAuthenticator();

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();


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
        System.out.println("return Withdrawal Cancel ConfigProperties ");
        return configProperties;
    }

    @Override
    public String getHelpText() {
        System.out.println("getHelpText called ...");
        return "Withdrawal Cancel Page will be up to User who requested Withdrawal";
    }

    @Override
    public String getDisplayType() {
        System.out.println("getDisplayType called ... returning Withdrawal Cancel");
        return "Password Update Alert";
    }

    @Override
    public String getReferenceCategory() {
        System.out.println("getReferenceCategory called ... returning withdrawal-cancel");
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