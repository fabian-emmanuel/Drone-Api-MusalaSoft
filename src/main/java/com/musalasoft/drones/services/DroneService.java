package com.musalasoft.drones.services;

import com.musalasoft.drones.dtos.BatteryLevelResponseDTO;
import com.musalasoft.drones.dtos.DroneRequestDTO;
import com.musalasoft.drones.dtos.DroneResponseDTO;
import com.musalasoft.drones.dtos.LoadMedicationItemsRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DroneService {
    Mono<DroneResponseDTO> registerDrone(DroneRequestDTO droneRequestDTO);
    Flux<DroneResponseDTO> retrieveAllDrones();
    Mono<DroneResponseDTO> retrieveDroneById(String id);
    Mono<Void> loadMedicationItems(LoadMedicationItemsRequestDTO loadMedicationItemsRequestDTO);
    Mono<List<DroneResponseDTO>> retrieveAvailableDronesForLoading();
    Mono<List<DroneResponseDTO>> retrieveDronesByState(String state);
    Mono<BatteryLevelResponseDTO> retrieveDroneBatteryLevel(String droneId);
}
