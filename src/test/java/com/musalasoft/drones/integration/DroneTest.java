package com.musalasoft.drones.integration;


import com.musalasoft.drones.Utils.AbstractTest;
import com.musalasoft.drones.Utils.TestModels;
import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.DroneRequestDTO;
import com.musalasoft.drones.dtos.LoadMedicationItemsRequestDTO;
import com.musalasoft.drones.enums.Model;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.models.Drone;
import com.musalasoft.drones.models.Medication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.Set;

import static com.musalasoft.drones.utils.CustomUtil.FAKER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class DroneTest extends AbstractTest {

    @Test
    void testRegisterDrone() {

        DroneRequestDTO droneRequestDTO = DroneRequestDTO.builder()
                .weightLimit(FAKER.number().randomDouble(2, 1, 500))
                .batteryCapacity(FAKER.number().numberBetween(0, 100))
                .build();



        webTestClient.post()
                .uri("/drones/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(droneRequestDTO), DroneRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(Message.DRONE_REGISTERED_SUCCESSFULLY)
                .jsonPath("$.data.id").isNotEmpty()
                .jsonPath("$.data.serialNumber").isNotEmpty()
                .jsonPath("$.data.model").isEqualTo(Model.getModelByWeight(droneRequestDTO.weightLimit()).name())
                .jsonPath("$.data.weightLimit").isEqualTo(droneRequestDTO.weightLimit())
                .jsonPath("$.data.batteryCapacity").isEqualTo(droneRequestDTO.batteryCapacity())
                .jsonPath("$.data.state").isEqualTo(State.IDLE.name())
                .jsonPath("$.data.createdAt").isNotEmpty()
                .jsonPath("$.data.medications").isArray();

        droneRepository.count()
                .doOnNext(count -> {
                    assertEquals(1L, count);
                }).block();
    }

    @Test
    void retrieveAllRegisteredDrones() {
        Drone drone1 = TestModels.drone("dr-001",20d, 70);
        droneRepository.save(drone1).subscribe();

        Drone drone2 = TestModels.drone("dr-002",100d, 70);
        droneRepository.save(drone2).subscribe();

        Medication medication = TestModels.medication("M-001", 40.0);
        medicationRepository.save(medication).subscribe();

        droneService.loadMedicationItems(LoadMedicationItemsRequestDTO.builder()
                        .droneId(drone2.getId())
                        .medicationIds(Set.of(medication.getId()))
                .build()).subscribe();

        webTestClient.get()
                .uri("/drones")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo(Message.DRONE_RETRIEVED_SUCCESSFULLY)
                .jsonPath("$.data.size()").isEqualTo(2)

                .jsonPath("$.data[0].id").isEqualTo(drone1.getId())
                .jsonPath("$.data[0].serialNumber").isNotEmpty()
                .jsonPath("$.data[0].weightLimit").isEqualTo(drone1.getWeightLimit())
                .jsonPath("$.data[0].model").isEqualTo(Model.getModelByWeight(drone1.getWeightLimit()).name())
                .jsonPath("$.data[0].batteryCapacity").isEqualTo(drone1.getBatteryCapacity())
                .jsonPath("$.data[0].state").isEqualTo(State.IDLE.name())
                .jsonPath("$.data[0].medications.size()").isEqualTo(0)

                .jsonPath("$.data[1].id").isEqualTo(drone2.getId())
                .jsonPath("$.data[1].serialNumber").isNotEmpty()
                .jsonPath("$.data[1].weightLimit").isEqualTo(drone2.getWeightLimit())
                .jsonPath("$.data[1].model").isEqualTo(Model.getModelByWeight(drone2.getWeightLimit()).name())
                .jsonPath("$.data[1].batteryCapacity").isEqualTo(drone2.getBatteryCapacity() - 5)
                .jsonPath("$.data[1].state").isEqualTo(State.LOADING.name())
                .jsonPath("$.data[1].medications.size()").isEqualTo(1)
                .jsonPath("$.data[1].medications[0].id").isEqualTo(medication.getId())
                .jsonPath("$.data[1].medications[0].name").isEqualTo(medication.getName())
                .jsonPath("$.data[1].medications[0].code").isEqualTo(medication.getCode())
                .jsonPath("$.data[1].medications[0].weight").isEqualTo(medication.getWeight())
                .jsonPath("$.data[1].medications[0].imageUrl").isEqualTo(medication.getImageUrl())
                .jsonPath("$.data[1].medications[0].loaded").isEqualTo(true);

        droneRepository.count()
                .doOnNext(count -> {
                    assertEquals(2L, count);
                }).block();

        medicationRepository.count()
                .doOnNext(count -> {
                    assertEquals(1L, count);
                }).block();
    }


}
