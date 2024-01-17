package com.musalasoft.drones.dtos;

import com.musalasoft.drones.enums.State;
import lombok.Builder;

@Builder
public record EventLogResponseDTO(
        String droneId,
        int batteryCapacity,
        State state,
        Double weightLimit,
        Double loadedItemsWeight,
        String createdAt
) {
}
