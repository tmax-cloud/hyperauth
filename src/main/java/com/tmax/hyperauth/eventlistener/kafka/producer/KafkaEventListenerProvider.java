package com.tmax.hyperauth.eventlistener.kafka.producer;

import com.google.gson.JsonObject;
import com.tmax.hyperauth.caller.HyperAuthCaller;
import com.tmax.hyperauth.eventlistener.TopicEvent;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.timer.TimerSpi;

import javax.ws.rs.core.Context;

/**
 * @author taegeon_woo@tmax.co.kr
 */
@Slf4j
public class KafkaEventListenerProvider extends TimerSpi implements EventListenerProvider {
    @Context
    private final KeycloakSession session;
    public KafkaEventListenerProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        String userName = "unKnown";
        if (event.getDetails() != null && event.getDetails().get("username")!= null){
            userName = event.getDetails().get("username");
        } else if (event.getDetails() != null && event.getDetails().get("email")!= null){
            userName = event.getDetails().get("email");
        } else if (event.getUserId() != null){
            userName = session.users().getUserById(event.getUserId(), session.realms().getRealm(event.getRealmId())).getUsername();
        }

        TopicEvent topicEvent = TopicEvent.makeTopicEvent(event, userName);

        // TOPIC Event Publish !!
        try {
            KafkaProducer.publishEvent(session.getContext().getRealm().getId(), topicEvent);
        } catch (Exception e) {
            log.error("Error Occurs!!", e);
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
        switch (adminEvent.getOperationType().toString()) {
            case "DELETE":
                if (adminEvent.getResourcePath().startsWith("users")){ //FIXME : Delete Later !!!!
                    try {
//                      important : session에는 이미 user가 지워져서 user 정보를 들고 올수 없음 그래서 http콜로 한다!
                        String accessToken = HyperAuthCaller.loginAsAdmin();
                        JsonObject user = HyperAuthCaller.getUser(adminEvent.getResourcePath().toString().substring(6), accessToken.replaceAll("\"", ""));
                        if ( user.get("username")!= null ){ // admin console에서 identity provider 삭제시 에러발생으로 인해 추가
                            // Topic Event
                            TopicEvent topicEvent = TopicEvent.makeOtherTopicEvent("USER_DELETE", user.get("username").toString().replaceAll("\"", ""), adminEvent.getTime());
                            KafkaProducer.publishEvent("tmax", topicEvent);
                        }
                    } catch (Exception e) {
                        log.error("Error Occurs!!", e);
                    }
                }
                break;
        }
    }

    @Override
    public void close() {

    }
}