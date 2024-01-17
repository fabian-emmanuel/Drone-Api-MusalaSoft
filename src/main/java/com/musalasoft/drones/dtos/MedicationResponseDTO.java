package com.musalasoft.drones.dtos;

import lombok.Builder;

@Builder
public record MedicationResponseDTO(
        String id,
        String name,
        String code,
        Double weight,
        String imageUrl,
        boolean loaded,
        String createdAt
) {
}
