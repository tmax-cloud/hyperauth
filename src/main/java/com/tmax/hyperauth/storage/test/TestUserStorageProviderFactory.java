package com.tmax.hyperauth.storage.test;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

@Slf4j
public class TestUserStorageProviderFactory  implements UserStorageProviderFactory<TestUserStorageProvider> {

    @Override
    public TestUserStorageProvider create(KeycloakSession session, ComponentModel model) {
// here you can setup the user storage provider, initiate some connections, etc.
        log.debug("CreateProvider ");

        UserRepository repository = new UserRepository();

        return new TestUserStorageProvider(session, model, repository);    }

    @Override
    public String getId() {
        log.debug("getId ");
        return "woo-user-storage";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        log.debug("getConfigProperties ");
        // this configuration is configurable in the admin-console
        return ProviderConfigurationBuilder.create()
                .property()
                .name("myParam")
                .label("My Param")
                .helpText("Some Description")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("some value")
                .add()
                // more properties
                // .property()
                // .add()
                .build();
    }
}
