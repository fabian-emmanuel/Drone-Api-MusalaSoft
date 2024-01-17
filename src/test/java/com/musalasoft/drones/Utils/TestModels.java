package com.musalasoft.drones.Utils;

import com.musalasoft.drones.dtos.DroneRequestDTO;
import com.musalasoft.drones.dtos.EventLogResponseDTO;
import com.musalasoft.drones.dtos.MedicationRequestDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.enums.Model;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.models.Drone;
import com.musalasoft.drones.models.EventLog;
import com.musalasoft.drones.models.Medication;
import com.musalasoft.drones.utils.DateUtil;

import java.time.LocalDateTime;
import java.util.HashSet;

import static com.musalasoft.drones.utils.CustomUtil.FAKER;

public interface TestModels {
    static Drone drone(DroneRequestDTO droneRequestDTO){
        return Drone.builder()
                .id(FAKER.random().hex())
                .serialNumber(FAKER.random().hex())
                .weightLimit(droneRequestDTO.weightLimit())
                .model(Model.getModelByWeight(droneRequestDTO.weightLimit()))
                .batteryCapacity(droneRequestDTO.batteryCapacity())
                .state(State.IDLE)
                .createdAt(LocalDateTime.now())
                .medicationIds(new HashSet<>())
                .build();
    }

    static Drone drone(String id, Double weightLimit, int batteryCapacity){
        return Drone.builder()
                .id(id)
                .serialNumber(FAKER.random().hex())
                .weightLimit(weightLimit)
                .model(Model.getModelByWeight(weightLimit))
                .batteryCapacity(batteryCapacity)
                .state(State.IDLE)
                .createdAt(LocalDateTime.now())
                .medicationIds(new HashSet<>())
                .build();

    }

    static Medication medication(MedicationRequestDTO request) {
        return Medication.builder()
                .id(FAKER.random().hex())
                .code(request.code())
                .name(request.name())
                .weight(request.weight())
                .imageUrl(request.imageUrl())
                .createdAt(LocalDateTime.now())
                .build();
    }

    static Medication medication(String id, Double weight) {
        return Medication.builder()
                .id(id)
                .code(FAKER.random().hex())
                .name(FAKER.medical().medicineName())
                .weight(weight)
                .imageUrl(FAKER.internet().image())
                .loaded(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    static MedicationResponseDTO medicationResponseDTO(Medication request) {
        return MedicationResponseDTO.builder()
                .id(request.getId())
                .code(request.getCode())
                .name(request.getName())
                .weight(request.getWeight())
                .imageUrl(request.getImageUrl())
                .loaded(request.isLoaded())
                .createdAt(DateUtil.formatLocalDateTime(request.getCreatedAt()))
                .build();
    }

    static EventLog eventLog(){
        return EventLog.builder()
                .id(FAKER.random().hex())
                .droneId(FAKER.random().hex())
                .loadedItemsWeight(FAKER.number().randomDouble(2, 50, 200))
                .batteryCapacity(FAKER.number().numberBetween(0,100))
                .createdAt(LocalDateTime.now())
                .build();
    }

    static EventLogResponseDTO eventLogResponseDTO(EventLog eventLog){
        return EventLogResponseDTO.builder()
                .droneId(eventLog.getDroneId())
                .batteryCapacity(eventLog.getBatteryCapacity())
                .state(eventLog.getState())
                .weightLimit(eventLog.getWeightLimit())
                .loadedItemsWeight(eventLog.getLoadedItemsWeight())
                .createdAt(DateUtil.formatLocalDateTime(eventLog.getCreatedAt()))
                .build();
    }
}
