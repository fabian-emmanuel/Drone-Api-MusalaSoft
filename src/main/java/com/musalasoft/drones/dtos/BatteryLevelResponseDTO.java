package com.musalasoft.drones.dtos;

import com.musalasoft.drones.enums.State;
import lombok.Builder;

@Builder
public record BatteryLevelResponseDTO(
        State state,
        int batteryLevel
) {
}
