package com.tmax.hyperauth.eventlistener.queue;

import com.tmax.hyperauth.eventlistener.provider.HyperauthEvent;
import com.tmax.hyperauth.eventlistener.provider.HyperauthEventPublisher;
import com.tmax.hyperauth.eventlistener.provider.ProviderConstants;

import org.keycloak.events.Event;
import org.keycloak.events.admin.AdminEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * consumer of in-memory queue. based on the EventPublisher type,
 * further processing of the events will be done.
 *
 * @author taegeon_woo@tmax.co.kr
 */

public class EventsConsumer {
    private final ScheduledExecutorService executorService;
    private final HyperauthEventPublisher publisher;
    private boolean isShutdownRequested = false;

    /**
     * @param publisher
     */
    public EventsConsumer(HyperauthEventPublisher publisher) {
        this.publisher = publisher;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Initialized a simple events consumer to consume keycloak events from queue");
    }

    /**
     * This shall be invoked once the providers are discovered.
     */
    public void init() {
        executorService.schedule(this::handleEvent, ProviderConstants.EXECUTOR_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * This shall be invoked before completing the server shutdown. This is needed
     * in order to gracefully shutdown the processing
     */
    public void shutdown() {
        isShutdownRequested = true;
        executorService.shutdown();

        System.out.println("Shutdown has been requested. Will shutdown once the processing has been completed");


        while (!executorService.isTerminated()) {
            try {
                System.out.println("Still processing the queue. will check again in a moment");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error!!, Consumer interrupted");
            }
        }
    }

    /**
     * peek from the queue and post the data to remote HTTP endpoint.
     * This will remove from the queue only if the response is success.
     * Otherwise this will retry indefinitely until a shutdown signal is received.
     */
    private void handleEvent() {
        while (!isShutdownRequested) {
//            if (log.isTraceEnabled()) {
//                log.trace("inside handle event");
//            }
            HyperauthEvent event = QueueHolder.getQueue().peek();
            if (event != null) {
//                if (log.isTraceEnabled()) {
//                    log.tracef("received %s", event.getEvent());
//                }
                boolean isAdminEvent = event.getEvent() instanceof AdminEvent;
                if (isAdminEvent) {
                    if (!publisher.sendEvent((AdminEvent) event.getEvent())) {
                        continue;
                    }
                } else if (!publisher.sendEvent((Event) event.getEvent())) {
                    continue;
                }
                QueueHolder.getQueue().remove();
            }
        }
    }
}
