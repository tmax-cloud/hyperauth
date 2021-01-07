package com.tmax.hyperauth.eventlistener.provider;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HyperauthEvent<T> {
    private T event;
}
