package com.musalasoft.drones.services;

import com.musalasoft.drones.dtos.MedicationRequestDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface MedicationService {
    Mono<MedicationResponseDTO> registerMedication(MedicationRequestDTO medicationRequestDTO);
    Mono<List<MedicationResponseDTO>> retrieveAllMedications();
    Mono<List<MedicationResponseDTO>> retrieveAllMedicationsByIdIn(Set<String> medicationIds);
    Mono<List<MedicationResponseDTO>> retrieveAllUnloadedMedicationsByIdIn(Set<String> medicationIds);
    Mono<Void> updateLoadedStatusForMedicationsWithIds(Set<String> medicationIds);
    Mono<List<MedicationResponseDTO>> retrieveUnloadedMedications();
}
