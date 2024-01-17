package com.musalasoft.drones.mappers;

import com.musalasoft.drones.dtos.MedicationRequestDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.models.Medication;

import static com.musalasoft.drones.utils.DateUtil.formatLocalDateTime;

public interface MedicationMapper {
    static Medication mapToMedication(MedicationRequestDTO medicationRequestDTO){
        return Medication.builder()
                .name(medicationRequestDTO.name())
                .code(medicationRequestDTO.code())
                .weight(medicationRequestDTO.weight())
                .imageUrl(medicationRequestDTO.imageUrl())
                .loaded(false)
                .build();
    }

    static MedicationResponseDTO mapToMedicationResponseDTO(Medication medication){
        return MedicationResponseDTO.builder()
                .id(medication.getId())
                .name(medication.getName())
                .code(medication.getCode())
                .weight(medication.getWeight())
                .imageUrl(medication.getImageUrl())
                .loaded(medication.isLoaded())
                .createdAt(formatLocalDateTime(medication.getCreatedAt()))
                .build();
    }
}
