package com.musalasoft.drones.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record ResponseBody<T>(
        @JsonProperty
        boolean success,
        @JsonProperty
        String message,
        @JsonProperty
        @Nullable
        T data
) {
}
