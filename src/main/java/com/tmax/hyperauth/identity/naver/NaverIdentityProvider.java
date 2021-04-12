package com.tmax.hyperauth.identity.naver;

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
public class NaverIdentityProvider extends AbstractOAuth2IdentityProvider implements SocialIdentityProvider {
    public static final String AUTH_URL = "https://nid.naver.com/oauth2.0/authorize";
    public static final String TOKEN_URL = "https://nid.naver.com/oauth2.0/token";
    public static final String PROFILE_URL = "https://openapi.naver.com/v1/nid/me";
    public static final String DEFAULT_SCOPE = "basic";

    public NaverIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
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
        BrokeredIdentityContext user = new BrokeredIdentityContext(profile.get("response").get("id").asText());

        String email = profile.get("response").get("email").asText();

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
            BrokeredIdentityContext user = extractIdentityFromProfile(null, profile);
            return user;
        } catch (Exception e) {
            log.error("Error Occurs!!", e);
            throw new IdentityBrokerException("Could not obtain user profile from naver.", e);
        }
    }

    @Override
    protected String getDefaultScopes() {
        return "";
    }
}
