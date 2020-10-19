package com.tmax.hyperauth.jpa;

import org.keycloak.Config.Scope;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class HyperauthJpaEntityProviderFactory implements JpaEntityProviderFactory {
	protected static final String ID = "hyperauth-entity-provider";
	
    @Override
    public JpaEntityProvider create(KeycloakSession session) {
        return new HyperauthJpaEntityProvider();
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void init(Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }
}
