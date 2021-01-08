package com.tmax.hyperauth.eventlistener.provider;

public class TopicEvent {
    public static class Item {
        private String verb;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        private User user;
        private Status status;

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

    public static class Status {
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
        User user = new User();
        user.setUsername(username);
        item.setUser(user);
        Status status = new Status();
        status.setReason(reason);
        status.setCode(code);
        item.setStatus(status);
        return item;
    }
}
