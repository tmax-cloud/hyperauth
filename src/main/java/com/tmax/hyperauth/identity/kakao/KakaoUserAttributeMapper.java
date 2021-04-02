package com.tmax.hyperauth.identity.kakao;

import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;

public class KakaoUserAttributeMapper extends AbstractJsonUserAttributeMapper {

    private static final String[] cp = new String[] { KakaoIdentityProviderFactory.PROVIDER_ID };

    @Override
    public String[] getCompatibleProviders() {
        System.out.println("kakao : getCompatibleProviders()");
        return cp;
    }

    @Override
    public String getId() {
        System.out.println("kakao : getId()");
        return "kakao-user-attribute-mapper";
    }
}
