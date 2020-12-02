package com.tmax.hyperauth.passwordhash;

import org.keycloak.Config;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderFactory;

/**
 * @author <a href="mailto:taegeon_woo@tmax.co.kr">taegeon_woo</a>
 */
public class BCryptPasswordHashProviderFactory implements PasswordHashProviderFactory {
    public static final String ID = "bcrypt";
    public static final int DEFAULT_ITERATIONS = 10;

    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return new BCryptPasswordHashProvider(ID, DEFAULT_ITERATIONS);
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void close() {
    }
}
