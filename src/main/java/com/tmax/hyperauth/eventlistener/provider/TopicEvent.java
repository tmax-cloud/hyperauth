package com.tmax.hyperauth.eventlistener.provider;

public class TopicEvent {
    public static class Event {
        private String kind = "Event";
        private String apiVersion = "audit.k8s.io/v1beta1";
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

    public static Event makeTopicEvent(String verb, String username, String reason, int code) {
        Event event = new Event();
        event.setVerb(verb);
        User user = new User();
        user.setUsername(username);
        event.setUser(user);
        ResponseStatus status = new ResponseStatus();
        status.setReason(reason);
        status.setCode(code);
        event.setResponseStatus(status);
        return event;
    }
}
