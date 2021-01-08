package com.tmax.hyperauth.eventlistener.provider;

import org.keycloak.events.Event;

import java.util.List;
import java.util.Map;

public class EventDataObject {
    public static class Eventlists {
        private String kind = "Eventlists";
        private String apiVersion = "audit.k8s.io/v1beta1";
        private List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    public static class Item {
        private String verb;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public ResponseStatus getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(ResponseStatus responseStatus) {
            this.responseStatus = responseStatus;
        }

        private User user;
        private ResponseStatus responseStatus;

        public String getVerb() {
            return verb;
        }

        public void setVerb(String verb) {
            this.verb = verb;
        }
    }

    public static class User {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class ResponseStatus {
        private String reason;
        private int code;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static Item makeTopicEvent(String verb, String username, String reason, int code) {
        Item item = new Item();
        item.setVerb(verb);
        EventDataObject.User user = new EventDataObject.User();
        user.setUsername(username);
        item.setUser(user);
        EventDataObject.ResponseStatus responseStatus = new EventDataObject.ResponseStatus();
        responseStatus.setReason(reason);
        responseStatus.setCode(code);
        item.setResponseStatus(responseStatus);
        return item;
    }

    public static String toString(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append("verb=");
        sb.append(item.getVerb());
        sb.append(", username=");
        sb.append(item.getUser().getUsername());
        sb.append(", reason=");
        sb.append(item.getResponseStatus().getReason());
        return sb.toString();
    }
}
