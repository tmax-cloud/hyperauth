package com.tmax.hyperauth.eventlistener;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import javax.ws.rs.core.Context;

/**
 * @author taegeon_woo@tmax.co.kr
 */
@Slf4j
public class DuplicateLoginBlockProvider implements EventListenerProvider {
    @Context
    private final KeycloakSession session;
    public DuplicateLoginBlockProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType().toString().equals("LOGIN")) {// For Session-Restrict Policy
            log.info("User [ " + event.getDetails().get("username") + " ], Client [ " + event.getClientId() + " ] Session-Restrict Start");
            RealmModel realm = session.realms().getRealmByName(event.getRealmId());
            session.sessions().getUserSessions(realm, session.clients().getClientByClientId(realm, event.getClientId())).forEach(userSession -> {
                if (userSession.getUser().getUsername().equalsIgnoreCase(event.getDetails().get("username"))
                        && !userSession.getId().equals(event.getSessionId())) {
                    session.sessions().removeUserSession(realm, userSession);
                    log.info("Remove user session [ " + userSession.getId() + " ]");
                }
            });
            log.info("User [ " + event.getDetails().get("username") + " ], Client [ " + event.getClientId() + " ] Session-Restrict Success");
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {}

    @Override
    public void close() {}
}