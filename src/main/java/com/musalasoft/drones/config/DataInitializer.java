package com.musalasoft.drones.config;

import com.musalasoft.drones.dtos.DroneRequestDTO;
import com.musalasoft.drones.dtos.MedicationRequestDTO;
import com.musalasoft.drones.services.DroneService;
import com.musalasoft.drones.services.MedicationService;
import com.musalasoft.drones.utils.CustomUtil;
import com.musalasoft.drones.utils.GeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DroneService droneService;
    private final MedicationService medicationService;

    @Override
    public void run(String... args) {
        List<DroneRequestDTO> droneRequestDTOS = generateDroneRequestDTOs();
        List<MedicationRequestDTO> medicationRequestDTOS = generateMedicationRequestDTOs();
        droneRequestDTOS.forEach(droneRequestDTO -> droneService.registerDrone(droneRequestDTO).subscribe());
        medicationRequestDTOS.forEach(medicationRequestDTO -> medicationService.registerMedication(medicationRequestDTO).subscribe());
    }

    private List<DroneRequestDTO> generateDroneRequestDTOs() {
        log.info("Initializing Default Drones");
        List<DroneRequestDTO> droneRequestDTOS = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            droneRequestDTOS.add(DroneRequestDTO.builder()
                    .weightLimit(CustomUtil.FAKER.number().randomDouble(2, 45, 500))
                    .batteryCapacity(CustomUtil.FAKER.number().numberBetween(20, 100))
                    .build());
        }
        return droneRequestDTOS;
    }

    private List<MedicationRequestDTO> generateMedicationRequestDTOs() {
        log.info("Initializing Default Medications");
        List<MedicationRequestDTO> medicationRequestDTOS = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            medicationRequestDTOS.add(MedicationRequestDTO.builder()
                    .name(CustomUtil.FAKER.medical().medicineName())
                    .code(GeneratorUtil.uniqueReference("MED", 7))
                    .weight(CustomUtil.FAKER.number().randomDouble(2, 5, 200))
                    .imageUrl(CustomUtil.FAKER.internet().image())
                    .build());
        }
        return medicationRequestDTOS;
    }
}
