package com.tmax.hyperauth.eventlistener.queue;

import com.tmax.hyperauth.eventlistener.provider.HyperauthEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * In-memory blocking queue which will hold the events at first place and
 * then can be queried for processing.
 *
 * @author taegeon_woo@tmax.co.kr
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueueHolder {

    private static final BlockingDeque<HyperauthEvent> LINKED_BLOCKING_DEQUE = new LinkedBlockingDeque<>();

    /**
     * returns the singleton LINKED_BLOCKING_DEQUE instance
     *
     * @return the singleton LINKED_BLOCKING_DEQUE instance
     */
    public static BlockingDeque<HyperauthEvent> getQueue() {
        return LINKED_BLOCKING_DEQUE;
    }
}
