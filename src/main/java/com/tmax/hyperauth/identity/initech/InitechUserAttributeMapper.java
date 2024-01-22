package com.tmax.hyperauth.identity.initech;

import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;

public class InitechUserAttributeMapper extends AbstractJsonUserAttributeMapper {

    private static final String[] cp = new String[] { InitechIdentityProviderFactory.PROVIDER_ID };

    @Override
    public String[] getCompatibleProviders() {
        return cp;
    }

    @Override
    public String getId() {
        return "initech-user-attribute-mapper";
    }
}
