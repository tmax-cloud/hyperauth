package com.tmax.hyperauth.identity.naver;

import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;

public class NaverUserAttributeMapper extends AbstractJsonUserAttributeMapper {

    private static final String[] cp = new String[] { NaverIdentityProviderFactory.PROVIDER_ID };

    @Override
    public String[] getCompatibleProviders() {
        return cp;
    }

    @Override
    public String getId() {
        return "naver-user-attribute-mapper";
    }
}
