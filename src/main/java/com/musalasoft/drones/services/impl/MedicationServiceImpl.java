package com.musalasoft.drones.services.impl;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.MedicationRequestDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.exceptions.DuplicateException;
import com.musalasoft.drones.mappers.MedicationMapper;
import com.musalasoft.drones.repositories.MedicationRepository;
import com.musalasoft.drones.services.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    @Override
    public Mono<MedicationResponseDTO> registerMedication(MedicationRequestDTO medicationRequestDTO) {
        return medicationRepository.existsMedicationByCode(medicationRequestDTO.code())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateException(String.format(Message.MEDICATION_EXISTS, medicationRequestDTO.code())));
                    }

                    return medicationRepository.save(MedicationMapper.mapToMedication(medicationRequestDTO))
                            .map(MedicationMapper::mapToMedicationResponseDTO);
                });
    }

    @Override
    public Mono<List<MedicationResponseDTO>> retrieveAllMedications() {
        return medicationRepository.findAll()
                .map(MedicationMapper::mapToMedicationResponseDTO)
                .collectList();
    }

    @Override
    public Mono<List<MedicationResponseDTO>> retrieveAllMedicationsByIdIn(Set<String> medicationIds) {
        return medicationRepository.findAllByIdIn(medicationIds)
                .map(MedicationMapper::mapToMedicationResponseDTO)
                .collectList();
    }

    @Override
    public Mono<List<MedicationResponseDTO>> retrieveAllUnloadedMedicationsByIdIn(Set<String> medicationIds) {
        return medicationRepository.findAllByIdInAndLoadedIs(medicationIds, false)
                .map(MedicationMapper::mapToMedicationResponseDTO)
                .collectList();
    }

    @Override
    public Mono<Void> updateLoadedStatusForMedicationsWithIds(Set<String> medicationIds) {
        return medicationRepository.findAllByIdIn(medicationIds)
                .map(medication -> {
                    medication.setLoaded(true);
                    return medication;
                })
                .collectList()
                .flatMap(medications -> medicationRepository.saveAll(medications).then());
    }

    @Override
    public Mono<List<MedicationResponseDTO>> retrieveUnloadedMedications() {
        return medicationRepository.findAllByLoadedIs(false)
                .map(MedicationMapper::mapToMedicationResponseDTO)
                .collectList();
    }
}
