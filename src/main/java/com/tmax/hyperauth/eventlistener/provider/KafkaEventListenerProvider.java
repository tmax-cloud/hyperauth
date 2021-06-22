package com.tmax.hyperauth.eventlistener.provider;

import com.google.gson.JsonObject;
import com.tmax.hyperauth.authenticator.AuthenticatorConstants;
import com.tmax.hyperauth.caller.HyperAuthCaller;
import com.tmax.hyperauth.caller.HypercloudOperatorCaller;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.util.Time;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.timer.TimerProvider;
import org.keycloak.timer.TimerSpi;

import javax.ws.rs.core.Context;
import java.util.Arrays;
import java.util.Map;

/**
 * @author taegeon_woo@tmax.co.kr
 */
@Slf4j
public class KafkaEventListenerProvider extends TimerSpi implements EventListenerProvider {
    @Context
    private KeycloakSession session;
    public KafkaEventListenerProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        System.out.println("kafka events occurred");
        String userName = "";
            TopicEvent topicEvent = TopicEvent.makeTopicEvent(event, null);

        switch (event.getType().toString()) {
            case "REGISTER":
                topicEvent = TopicEvent.makeTopicEvent(event, event.getDetails().get("username"));
                break;
            case "LOGIN":
                topicEvent = TopicEvent.makeTopicEvent(event, event.getDetails().get("username"));
                break;
            case "LOGIN_ERROR":
                if (event.getDetails() != null && event.getDetails().get("username")!= null){
                    topicEvent = TopicEvent.makeTopicEvent(event, event.getDetails().get("username"));
                }
                break;
            case "LOGOUT":
                topicEvent = TopicEvent.makeTopicEvent(event, session.users().getUserById(event.getUserId(), session.realms().getRealm(event.getRealmId())).getUsername());
                break;
        }
        // TOPIC Event Publish !!
        try {
            Producer.publishEvent("tmax", topicEvent);
        } catch (Exception e) {
            log.error("Error Occurs!!", e);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
    }

    @Override
    public void close() {

    }
}