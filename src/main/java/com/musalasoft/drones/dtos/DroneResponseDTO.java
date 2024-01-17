package com.musalasoft.drones.dtos;

import com.musalasoft.drones.enums.Model;
import com.musalasoft.drones.enums.State;
import lombok.Builder;

import java.util.Collection;

@Builder
public record DroneResponseDTO(
        String id,
        String serialNumber,
        Model model,
        Double weightLimit,
        int batteryCapacity,
        State state,
        String createdAt,
        Collection<MedicationResponseDTO> medications
) {}
