package com.musalasoft.drones.services.impl;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.*;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.exceptions.InvalidRequestException;
import com.musalasoft.drones.exceptions.ResourceNotFoundException;
import com.musalasoft.drones.mappers.DroneMapper;
import com.musalasoft.drones.models.Drone;
import com.musalasoft.drones.repositories.DroneRepository;
import com.musalasoft.drones.services.DroneService;
import com.musalasoft.drones.services.MedicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationService medicationService;

    @Override
    public Mono<DroneResponseDTO> registerDrone(DroneRequestDTO droneRequestDTO) {
        return droneRepository.save(DroneMapper.mapToDrone(droneRequestDTO))
                .map(DroneMapper::mapToDroneResponseDTO);
    }

    @Override
    public Flux<DroneResponseDTO> retrieveAllDrones() {
        return droneRepository.findAll()
                .flatMap(drone -> medicationService.retrieveAllMedicationsByIdIn(drone.getMedicationIds())
                        .map(medications -> DroneMapper.mapToDroneResponseDTO(drone, medications)));
    }

    @Override
    public Mono<DroneResponseDTO> retrieveDroneById(String droneId) {
        return findDroneById(droneId)
                .flatMap(drone -> medicationService.retrieveAllMedicationsByIdIn(drone.getMedicationIds())
                        .map(medications -> DroneMapper.mapToDroneResponseDTO(drone, medications)));
    }

    @Override
    public Mono<Void> loadMedicationItems(LoadMedicationItemsRequestDTO loadMedicationItemsRequestDTO) {
        return findDroneById(loadMedicationItemsRequestDTO.droneId())
                .flatMap(drone -> {
                    if (drone.getBatteryCapacity() < 25) {
                        return Mono.error(new InvalidRequestException(Message.LOW_BATTERY));
                    }

                    return validateWeightAndSave(drone, loadMedicationItemsRequestDTO.medicationIds());
                });
    }

    @Override
    public Mono<List<DroneResponseDTO>> retrieveAvailableDronesForLoading() {
        return droneRepository.findAllByStateInAndBatteryCapacityGreaterThanEqual(List.of(State.IDLE, State.LOADING), 25)
                .flatMap(drone -> medicationService.retrieveAllMedicationsByIdIn(drone.getMedicationIds())
                        .map(medications -> DroneMapper.mapToDroneResponseDTO(drone, medications)))
                .collectList();
    }

    @Override
    public Mono<List<DroneResponseDTO>> retrieveDronesByState(String state) {
        return State.validateState(state)
                .flatMap(validatedState -> droneRepository.findAllByStateIs(validatedState)
                        .flatMap(drone -> medicationService.retrieveAllMedicationsByIdIn(drone.getMedicationIds())
                                .map(medications -> DroneMapper.mapToDroneResponseDTO(drone, medications)))
                        .collectList());
    }

    @Override
    public Mono<BatteryLevelResponseDTO> retrieveDroneBatteryLevel(String droneId) {
        return findDroneById(droneId)
                .map(drone -> BatteryLevelResponseDTO.builder()
                        .state(drone.getState())
                        .batteryLevel(drone.getBatteryCapacity())
                        .build());
    }

    private Mono<Void> validateWeightAndSave(Drone drone, Set<String> medicationIds) {
        return medicationService.retrieveAllUnloadedMedicationsByIdIn(medicationIds)
                .flatMap(unLoadedMedications -> {

                    double unLoadedItemsWeight = sumMedicationsWeight(unLoadedMedications);
                    Set<String> unLoadedMedicationIds = unLoadedMedications.stream().map(MedicationResponseDTO::id).collect(Collectors.toSet());

                    log.info("Medications With Ids {}; Are Already Loaded On Another Drone", getMedicationIdsThatCouldNotBeLoaded(medicationIds, unLoadedMedicationIds));

                    if (drone.getMedicationIds().isEmpty()) {

                        if (unLoadedItemsWeight > drone.getWeightLimit()) {
                            return Mono.error(new InvalidRequestException(String.format(Message.WEIGHT_LIMIT_EXCEEDED, unLoadedItemsWeight - drone.getWeightLimit())));
                        }

                        return updateDroneStateAndSave(drone, unLoadedMedicationIds, unLoadedItemsWeight);
                    }

                    return medicationService.retrieveAllMedicationsByIdIn(drone.getMedicationIds())
                            .flatMap(existingMedications -> {

                                double existingItemsWeight = sumMedicationsWeight(existingMedications);
                                double totalPotentialWeight = existingItemsWeight + unLoadedItemsWeight;

                                if (totalPotentialWeight > drone.getWeightLimit()) {
                                    return Mono.error(new InvalidRequestException(String.format(Message.WEIGHT_LIMIT_EXCEEDED, totalPotentialWeight - drone.getWeightLimit())));
                                }

                                return updateDroneStateAndSave(drone, unLoadedMedicationIds, unLoadedItemsWeight);
                            });
                });
    }

    private Set<String> getMedicationIdsThatCouldNotBeLoaded(Set<String> medicationIds, Set<String> unLoadedMedicationIds) {
        Set<String> difference = new HashSet<>(medicationIds);
        difference.removeAll(unLoadedMedicationIds);
        return difference;
    }

    private static double sumMedicationsWeight(List<MedicationResponseDTO> unLoadedMedications) {
        return unLoadedMedications.stream().mapToDouble(MedicationResponseDTO::weight).sum();
    }

    private Mono<Void> updateDroneStateAndSave(Drone drone, Set<String> medicationIds, double itemsWeight) {

        if ((drone.getWeightLimit() - itemsWeight) <= (drone.getWeightLimit() * 0.20)) {
            drone.getMedicationIds().addAll(medicationIds);
            drone.setState(State.LOADED);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 5);
        } else {
            drone.getMedicationIds().addAll(medicationIds);
            drone.setState(State.LOADING);
            drone.setBatteryCapacity(drone.getBatteryCapacity() - 5);
        }

        return droneRepository.save(drone)
                .flatMap(updatedDrone -> medicationService.updateLoadedStatusForMedicationsWithIds(medicationIds)
                        .then());
    }

    private Mono<Drone> findDroneById(String droneId) {
        return droneRepository.findDroneById(droneId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(String.format(Message.DRONE_DOES_NOT_EXIST, droneId))));
    }
}
