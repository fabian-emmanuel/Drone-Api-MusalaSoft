package com.musalasoft.drones.dtos;

import com.musalasoft.drones.constants.Message;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Set;

@Builder
public record LoadMedicationItemsRequestDTO(
        @NotBlank(message = Message.DRONE_ID_IS_REQUIRED)
        String droneId,
        @NotNull(message = Message.MEDICATION_ID_IS_REQUIRED)
        Set<String> medicationIds
) {
}
