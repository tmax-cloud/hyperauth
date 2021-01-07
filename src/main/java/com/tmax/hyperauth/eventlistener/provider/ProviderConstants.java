package com.tmax.hyperauth.eventlistener.provider;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public class ProviderConstants {
    public static final String PUBLISHER_TYPE = "type";
    public static final String JMS_CONNECTION_FACTORY = "jmsConnectionFactory";
    public static final String JMS_EVENT_TOPIC = "jmsTopicEvent";
    public static final String JMS_ADMIN_EVENT_TOPIC = "jmsTopicAdminEvent";
    public static final int EXECUTOR_DELAY_SECONDS = 90;

    private ProviderConstants() {
    }
}
