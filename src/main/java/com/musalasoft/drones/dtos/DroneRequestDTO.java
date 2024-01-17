package com.musalasoft.drones.dtos;

import com.musalasoft.drones.constants.Message;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record DroneRequestDTO(
        @Positive
        @Max(value = 500, message = Message.INVALID_DRONE_WEIGHT_LIMIT)
        double weightLimit,
        @PositiveOrZero
        @Max(value = 100, message = Message.INVALID_BATTERY_CAPACITY)
        int batteryCapacity
) {
}
