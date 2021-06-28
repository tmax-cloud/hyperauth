package com.tmax.hyperauth.eventlistener;

import org.keycloak.events.Event;
import org.keycloak.events.EventType;
import org.keycloak.models.KeycloakSession;

import java.util.Map;

public class TopicEvent {

    private String type;

    private String userName;

    private String userId;

    private long time;

    private String realmId;

    private String clientId;

    private String sessionId;

    private String ipAddress;

    private String error;

    private Map<String, String> details;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = maxLength(realmId, 255);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = maxLength(clientId, 255);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = maxLength(userId, 255);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = maxLength(userName, 255);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public static TopicEvent makeTopicEvent(Event keycloakEvent, String userName) {
        TopicEvent topicEvent = new TopicEvent();
        topicEvent.setUserName(userName);
        topicEvent.setTime(keycloakEvent.getTime());

        // for type, type conversion needs!
        String topicEventType = "";
        if ( keycloakEvent.getType().equals(EventType.UPDATE_PROFILE )){
            if (keycloakEvent.getDetails()!= null && keycloakEvent.getDetails().get("userWithdrawal") != null && keycloakEvent.getDetails().get("userWithdrawal").equalsIgnoreCase("t")) {
                topicEventType = "USER_WITHDRAWAL";
            } else if (keycloakEvent.getDetails()!= null && keycloakEvent.getDetails().get("userDelete") != null && keycloakEvent.getDetails().get("userWithdrawal").equalsIgnoreCase("f")) {
                topicEventType = "USER_WITHDRAWAL_CANCEL";
            } else if (keycloakEvent.getDetails()!= null && keycloakEvent.getDetails().get("userDelete") != null && keycloakEvent.getDetails().get("userDelete").equalsIgnoreCase("t")) {
                topicEventType = "USER_DELETE";
            } else {
                topicEventType = EventType.UPDATE_PROFILE.toString();
            }
        } else {
            topicEventType = keycloakEvent.getType().toString();
        }
        topicEvent.setType(topicEventType);

        if (keycloakEvent.getUserId() != null) topicEvent.setUserId(keycloakEvent.getUserId());
        if (keycloakEvent.getRealmId() != null) topicEvent.setRealmId(keycloakEvent.getRealmId());
        if (keycloakEvent.getClientId() != null) topicEvent.setClientId(keycloakEvent.getClientId());
        if (keycloakEvent.getSessionId() != null) topicEvent.setSessionId(keycloakEvent.getSessionId());
        if (keycloakEvent.getIpAddress() != null) topicEvent.setIpAddress(keycloakEvent.getIpAddress());
        if (keycloakEvent.getError() != null) topicEvent.setError(keycloakEvent.getError());
        if (keycloakEvent.getDetails() != null) topicEvent.setDetails(keycloakEvent.getDetails());
        return topicEvent;
    }

    public static TopicEvent makeOtherTopicEvent(String eventType, String userName, Long time) {
        TopicEvent topicEvent = new TopicEvent();

        topicEvent.setTime(time);
        topicEvent.setUserName(userName);
        topicEvent.setType(eventType);
        topicEvent.setRealmId("tmax");
        return topicEvent;
    }

    static String maxLength(String string, int length){
        if (string != null && string.length() > length) {
            return string.substring(0, length - 1);
        }
        return string;
    }
}
