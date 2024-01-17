package com.musalasoft.drones.controllers;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.*;
import com.musalasoft.drones.services.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/drones")
public class DroneController {

    private final DroneService droneService;

    @PostMapping("/register")
    public Mono<BaseResponse<DroneResponseDTO>> registerDrone(@Valid @RequestBody DroneRequestDTO droneRequestDTO) {

        return droneService.registerDrone(droneRequestDTO)
                .map(droneResponseDTO -> new BaseResponse<>(true, HttpStatus.CREATED, Message.DRONE_REGISTERED_SUCCESSFULLY, droneResponseDTO));
    }

    @GetMapping("/{droneId}")
    public Mono<BaseResponse<DroneResponseDTO>> retrieveDroneById(@PathVariable String droneId){

        return droneService.retrieveDroneById(droneId)
                .map(droneResponseDTO -> new BaseResponse<>(true, HttpStatus.OK, Message.DRONE_RETRIEVED_SUCCESSFULLY, droneResponseDTO));
    }

    @PostMapping("/load-medication-items")
    public Mono<BaseResponse<Void>> loadMedicationItems(@Valid @RequestBody LoadMedicationItemsRequestDTO loadMedicationItemsRequestDTO){

        return droneService.loadMedicationItems(loadMedicationItemsRequestDTO)
                .thenReturn(new BaseResponse<>(true, HttpStatus.OK, Message.ITEMS_LOADED_SUCCESSFULLY));
    }

    @GetMapping("/available-for-loading")
    public Mono<BaseResponse<?>> retrieveAvailableDronesForLoading(){

        return droneService.retrieveAvailableDronesForLoading()
                .map(droneResponseDTOList -> new BaseResponse<>(true, HttpStatus.OK, Message.DRONE_RETRIEVED_SUCCESSFULLY, droneResponseDTOList));
    }

    @GetMapping("/state/{state}")
    public Mono<BaseResponse<?>> retrieveDronesByState(@PathVariable String state){

        return droneService.retrieveDronesByState(state)
                .map(droneResponseDTOList -> new BaseResponse<>(true, HttpStatus.OK, Message.DRONE_RETRIEVED_SUCCESSFULLY, droneResponseDTOList));
    }

    @GetMapping("/battery-level/{droneId}")
    public Mono<BaseResponse<BatteryLevelResponseDTO>> retrieveDroneBatteryLevel(@PathVariable String droneId){

        return droneService.retrieveDroneBatteryLevel(droneId)
                .map(batteryLevelResponseDTO -> new BaseResponse<>(true, HttpStatus.OK, Message.DRONE_BATTERY_LEVEL_RETRIEVED_SUCCESSFULLY, batteryLevelResponseDTO));
    }
}
