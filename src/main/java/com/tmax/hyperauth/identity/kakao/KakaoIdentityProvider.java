package com.tmax.hyperauth.identity.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;

@Slf4j
public class KakaoIdentityProvider extends AbstractOAuth2IdentityProvider implements SocialIdentityProvider {
    public static final String AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
    public static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    public static final String PROFILE_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String DEFAULT_SCOPE = "basic";

    public KakaoIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
        super(session, config);

        config.setAuthorizationUrl(AUTH_URL);
        config.setTokenUrl(TOKEN_URL);
        config.setUserInfoUrl(PROFILE_URL);
    }

    @Override
    protected boolean supportsExternalExchange() {
        return true;
    }

    @Override
    protected String getProfileEndpointForValidation(EventBuilder event) {
        return PROFILE_URL;
    }

    @Override
    protected BrokeredIdentityContext extractIdentityFromProfile(EventBuilder event, JsonNode profile) {
        // getJsonProperty 는 Oidc 관련 파싱만 가능하므로 JsonNode 의 get 메소드를 이용해서 가져온다.
        // BrokeredIdentityContext user = new BrokeredIdentityContext(getJsonProperty(profile, "response"));
        BrokeredIdentityContext user = new BrokeredIdentityContext(profile.get("id").asText());
        String email = profile.get("kakao_account").get("email").asText();
        user.setIdpConfig(getConfig());
        user.setUsername(email);
        user.setEmail(email);
        user.setIdp(this);
        AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, getConfig().getAlias());
        return user;
    }

    @Override
    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
        try {
            JsonNode profile = SimpleHttp.doGet(PROFILE_URL, session).param("access_token", accessToken).asJson();
            log.info("httpGet Success!!");
            BrokeredIdentityContext user = extractIdentityFromProfile(null, profile);
            return user;
        } catch (Exception e) {
            log.error("Error Occurs!!", e);
            throw new IdentityBrokerException("Could not obtain user profile from kakao.", e);
        }
    }

    @Override
    protected String getDefaultScopes() {
        return "";
    }
}
