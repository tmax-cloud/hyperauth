package com.tmax.hyperauth.eventlistener.provider;

import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;

/**
 * @author taegeon_woo@tmax.co.kr
 */

public interface HyperauthEventPublisher {
    /**
     * publishes event
     *
     * @return true if operation is successful otherwise false
     */
    boolean sendEvent(Event event);

    /**
     * publishes admin event
     *
     * @return true if operation is successful otherwise false
     */
    boolean sendEvent(AdminEvent adminEvent);
}
