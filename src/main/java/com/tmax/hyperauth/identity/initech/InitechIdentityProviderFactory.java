package com.tmax.hyperauth.identity.initech;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

public class InitechIdentityProviderFactory extends AbstractIdentityProviderFactory<InitechIdentityProvider> implements SocialIdentityProviderFactory<InitechIdentityProvider> {
    public static final String PROVIDER_ID = "initech";

    @Override
    public String getName() {
        return "initech";
    }

    @Override
    public InitechIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new InitechIdentityProvider(session, new OAuth2IdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public OAuth2IdentityProviderConfig createConfig() {
        return new OAuth2IdentityProviderConfig();
    }
}
