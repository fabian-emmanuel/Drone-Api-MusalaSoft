package com.musalasoft.drones.enums;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.exceptions.InvalidRequestException;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public enum State {
    IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING;

    public static Mono<State> validateState(String state) {
        for (State validState : values()) {
            if (validState.name().equalsIgnoreCase(state)) {
                return Mono.just(validState);
            }
        }
        return Mono.error(new InvalidRequestException(String.format(Message.INVALID_STATE, state, Arrays.toString(State.values()))));
    }
}
