package com.musalasoft.drones.dtos;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.constants.Regex;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record MedicationRequestDTO(
        @NotBlank(message = Message.NAME_IS_REQUIRED)
        @Pattern(regexp = Regex.MED_NAME, message = Message.MED_NAME_ALLOWABLE_VALUES)
        String name,

        @NotNull(message = Message.WEIGHT_IS_REQUIRED)
        @Max(value = 450, message = Message.INVALID_MEDICATION_WEIGHT)
        @Positive
        Double weight,

        @NotBlank(message = Message.CODE_IS_REQUIRED)
        @Pattern(regexp = Regex.MED_CODE, message = Message.MED_CODE_ALLOWABLE_VALUES)
        String code,

        @NotNull(message = Message.IMAGE_IS_REQUIRED)
        String imageUrl
) {
}
