package com.musalasoft.drones.controllers;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.*;
import com.musalasoft.drones.services.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/medications")
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping("/register")
    public Mono<BaseResponse<MedicationResponseDTO>> registerMedication(@Valid @RequestBody MedicationRequestDTO medicationRequestDTO) {

        return medicationService.registerMedication(medicationRequestDTO)
                .map(medicationResponseDTO -> new BaseResponse<>(true, HttpStatus.CREATED, Message.MEDICATION_REGISTERED_SUCCESSFULLY, medicationResponseDTO));

    }

    @GetMapping("/all")
    public Mono<BaseResponse<?>> retrieveAllMedications() {

        return medicationService.retrieveAllMedications()
                .map(medicationResponseDTOList -> new BaseResponse<>(true, HttpStatus.OK, Message.MEDICATION_RETRIEVED_SUCCESSFULLY, medicationResponseDTOList));

    }

    @GetMapping("/unloaded")
    public Mono<BaseResponse<?>> retrieveUnloadedMedications() {

        return medicationService.retrieveUnloadedMedications()
                .map(medicationResponseDTOList -> new BaseResponse<>(true, HttpStatus.OK, Message.MEDICATION_RETRIEVED_SUCCESSFULLY, medicationResponseDTOList));

    }
}
