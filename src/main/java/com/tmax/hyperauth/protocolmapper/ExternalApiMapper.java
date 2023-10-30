package com.tmax.hyperauth.protocolmapper;


import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.IDToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Extenal GET API protocol mapper.
 */
@Slf4j
public class ExternalApiMapper extends AbstractOIDCProtocolMapper implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    /*
     * A config which keycloak uses to display a generic dialog to configure the token.
     */
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();
    private static final OkHttpClient client = new OkHttpClient();

    /*
     * The ID of the token mapper. Is public, because we need this id in our data-setup project to
     * configure the protocol mapper in keycloak.
     */
    public static final String PROVIDER_ID = "external-api-mapper";

    static {
        // The builtin protocol mapper let the user define under which claim name (key)
        // the protocol mapper writes its value. To display this option in the generic dialog
        // in keycloak, execute the following method.
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        // The builtin protocol mapper let the user define for which tokens the protocol mapper
        // is executed (access token, id token, user info). To add the config options for the different types
        // to the dialog execute the following method. Note that the following method uses the interfaces
        // this token mapper implements to decide which options to add to the config. So if this token
        // mapper should never be available for some sort of options, e.g. like the id token, just don't
        // implement the corresponding interface.
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, ExternalApiMapper.class);
        configProperties.add(createConfigProperty("externalUrl", "External Claim Get URL", "HTTP Get URL to Get External Claim", ProviderConfigProperty.STRING_TYPE, null));
    }

    private static ProviderConfigProperty createConfigProperty(String name, String label, String helpText, String type, String defaultValue) {
        ProviderConfigProperty property = new ProviderConfigProperty();
        property.setName(name);
        property.setLabel(label);
        property.setHelpText(helpText);
        property.setType(type);
        property.setDefaultValue(defaultValue);
        return property;
    }

    @Override
    public String getDisplayCategory() {
        return "External API Token Mapper";
    }

    @Override
    public String getDisplayType() {
        return "External API Token Mapper";
    }

    @Override
    public String getHelpText() {
        return "External API Token Mapper";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    protected void setClaim(final IDToken token,
                            final ProtocolMapperModel mappingModel,
                            final UserSessionModel userSession,
                            final KeycloakSession keycloakSession,
                            final ClientSessionContext clientSessionCtx) {

        // adds our data to the token. Uses the parameters like the claim name which were set by the user
        // when this protocol mapper was configured in keycloak. Note that the parameters which can
        // be configured in keycloak for this protocol mapper were set in the static intializer of this class.
        //
        Request request;
        String externalUrl = mappingModel.getConfig().get("externalUrl");
        //GET svc
        HttpUrl.Builder urlBuilder = HttpUrl.parse(externalUrl).newBuilder();
        String url = urlBuilder.build().toString();
        request = new Request.Builder().url(url).get().build();

        log.info(" request" + request);
        String result;
        try {
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            result = response.body().string();
            log.info(" External GET SVC Result : " + result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, result);
    }

}
