package com.tmax.hyperauth.eventlistener.prometheus;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;

import java.util.Map;

public class MetricsEventListener implements EventListenerProvider {
    private final static Logger logger = Logger.getLogger(MetricsEventListener.class);

    @Override
    public void onEvent(Event event) {
        switch (event.getType()) {
            case LOGIN:
                PrometheusExporter.instance().recordLogin(event);
                break;
            case CLIENT_LOGIN:
                PrometheusExporter.instance().recordClientLogin(event);
                break;
            case REGISTER:
                PrometheusExporter.instance().recordRegistration(event);
                break;
            case REFRESH_TOKEN:
                PrometheusExporter.instance().recordRefreshToken(event);
                break;
            case CODE_TO_TOKEN:
                PrometheusExporter.instance().recordCodeToToken(event);
                break;
            case REGISTER_ERROR:
                PrometheusExporter.instance().recordRegistrationError(event);
                break;
            case LOGIN_ERROR:
                PrometheusExporter.instance().recordLoginError(event);
                break;
            case CLIENT_LOGIN_ERROR:
                PrometheusExporter.instance().recordClientLoginError(event);
                break;
            case REFRESH_TOKEN_ERROR:
                PrometheusExporter.instance().recordRefreshTokenError(event);
                break;
            case CODE_TO_TOKEN_ERROR:
                PrometheusExporter.instance().recordCodeToTokenError(event);
                break;
            default:
                PrometheusExporter.instance().recordGenericEvent(event);
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        PrometheusExporter.instance().recordGenericAdminEvent(event);
    }

    @Override
    public void close() {
    }
}
