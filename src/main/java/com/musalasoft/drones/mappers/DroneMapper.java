package com.musalasoft.drones.mappers;

import com.musalasoft.drones.dtos.DroneRequestDTO;
import com.musalasoft.drones.dtos.DroneResponseDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.enums.Model;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.models.Drone;
import com.musalasoft.drones.utils.GeneratorUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.musalasoft.drones.utils.DateUtil.formatLocalDateTime;

public interface DroneMapper {
    static Drone mapToDrone(DroneRequestDTO droneRequestDTO){
        return Drone.builder()
                .serialNumber(GeneratorUtil.uniqueReference("SRN",10))
                .weightLimit(droneRequestDTO.weightLimit())
                .model(Model.getModelByWeight(droneRequestDTO.weightLimit()))
                .batteryCapacity(droneRequestDTO.batteryCapacity())
                .state(State.IDLE)
                .medicationIds(new HashSet<>())
                .build();
    }

    static DroneResponseDTO mapToDroneResponseDTO(Drone drone){
        return responseBuilder(drone)
                .medications(Collections.emptySet())
                .build();
    }

    static DroneResponseDTO mapToDroneResponseDTO(Drone drone, List<MedicationResponseDTO> medications){
        return responseBuilder(drone)
                .medications(medications)
                .build();
    }

    static DroneResponseDTO.DroneResponseDTOBuilder responseBuilder(Drone drone){
        return DroneResponseDTO.builder()
                .id(drone.getId())
                .serialNumber(drone.getSerialNumber())
                .model(drone.getModel())
                .weightLimit(drone.getWeightLimit())
                .batteryCapacity(drone.getBatteryCapacity())
                .state(drone.getState())
                .createdAt(formatLocalDateTime(drone.getCreatedAt()));
    }
}
