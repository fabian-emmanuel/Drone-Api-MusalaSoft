package com.musalasoft.drones.unit.services;

import com.musalasoft.drones.Utils.TestModels;
import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.DroneRequestDTO;
import com.musalasoft.drones.dtos.DroneResponseDTO;
import com.musalasoft.drones.dtos.LoadMedicationItemsRequestDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.enums.Model;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.exceptions.InvalidRequestException;
import com.musalasoft.drones.exceptions.ResourceNotFoundException;
import com.musalasoft.drones.mappers.DroneMapper;
import com.musalasoft.drones.models.Drone;
import com.musalasoft.drones.models.Medication;
import com.musalasoft.drones.repositories.DroneRepository;
import com.musalasoft.drones.services.MedicationService;
import com.musalasoft.drones.services.impl.DroneServiceImpl;
import com.musalasoft.drones.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.musalasoft.drones.utils.CustomUtil.FAKER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
class DroneServiceTest {
    @InjectMocks
    DroneServiceImpl droneService;
    @Mock
    DroneRepository droneRepository;
    @Mock
    MedicationService medicationService;
    @Captor
    ArgumentCaptor<DroneRequestDTO> droneRequestCaptor;

    @Test
    void shouldRegisterDroneSuccessfully() {
        var request = DroneRequestDTO.builder()
                .weightLimit(FAKER.number().randomDouble(2, 1, 500))
                .batteryCapacity(FAKER.number().numberBetween(0, 100))
                .build();

        var drone = TestModels.drone(request);

        when(droneRepository.save(any(Drone.class))).thenReturn(Mono.just(drone));

        StepVerifier.create(droneService.registerDrone(request))
                .expectNextMatches(droneResponseDTO -> {
                    assertNotNull(droneResponseDTO.id());
                    assertNotNull(droneResponseDTO.createdAt());
                    assertEquals(request.batteryCapacity(), droneResponseDTO.batteryCapacity());
                    assertEquals(request.weightLimit(), droneResponseDTO.weightLimit());
                    assertEquals(Model.getModelByWeight(request.weightLimit()), droneResponseDTO.model());
                    assertNotNull(droneResponseDTO.serialNumber());
                    assertEquals(State.IDLE, droneResponseDTO.state());
                    assertEquals(0, droneResponseDTO.medications().size());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(droneRepository, times(1)).save(any(Drone.class));
    }

    @Test
    void shouldRetrieveAllDronesSuccessfully() {

        Drone drone1 = TestModels.drone("D-001", 30.0, 80);
        Drone drone2 = TestModels.drone("D-002", 80.0, 70);

        when(droneRepository.findAll()).thenReturn(Flux.just(drone1, drone2));
        when(medicationService.retrieveAllMedicationsByIdIn(anySet())).thenReturn(Mono.just(Collections.emptyList()));

        DroneResponseDTO expectedDroneResponseDTO1 = DroneMapper.mapToDroneResponseDTO(drone1, Collections.emptyList());
        DroneResponseDTO expectedDroneResponseDTO2 = DroneMapper.mapToDroneResponseDTO(drone2, Collections.emptyList());

        StepVerifier.create(droneService.retrieveAllDrones())
                .expectNextMatches(response1 -> {
                    assertEquals(expectedDroneResponseDTO1, response1);
                    return true;
                })
                .expectNextMatches(response2 -> {
                    assertEquals(expectedDroneResponseDTO2, response2);
                    return true;
                })
                .verifyComplete();

        verify(droneRepository, times(1)).findAll();
        verify(medicationService, times(2)).retrieveAllMedicationsByIdIn(anySet());

    }

    @Test
    void shouldRetrieveEmptyFluxWhenRetrievingAllDronesAndNoRecordExist() {

        when(droneRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(droneService.retrieveAllDrones())
                .expectNextCount(0)
                .verifyComplete();

        verify(droneRepository, times(1)).findAll();
        verify(medicationService, never()).retrieveAllMedicationsByIdIn(anySet());
    }

    @Test
    void shouldRetrieveDroneByIdSuccessfully() {

        Drone drone = TestModels.drone("D-001", 30.0, 80);

        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.just(drone));
        when(medicationService.retrieveAllMedicationsByIdIn(anySet())).thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(droneService.retrieveDroneById(drone.getId()))
                .expectNextMatches(droneResponseDTO -> {
                    assertEquals(drone.getId(), droneResponseDTO.id());
                    assertEquals(DateUtil.formatLocalDateTime(drone.getCreatedAt()), droneResponseDTO.createdAt());
                    assertEquals(drone.getBatteryCapacity(), droneResponseDTO.batteryCapacity());
                    assertEquals(drone.getWeightLimit(), droneResponseDTO.weightLimit());
                    assertEquals(drone.getModel(), droneResponseDTO.model());
                    assertEquals(drone.getSerialNumber(), droneResponseDTO.serialNumber());
                    assertEquals(drone.getState(), droneResponseDTO.state());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(droneRepository, times(1)).findDroneById(anyString());
        verify(medicationService, times(1)).retrieveAllMedicationsByIdIn(anySet());
    }

    @Test
    void shouldReturnResourceNotFoundExceptionWhenRetrieveDroneByIdAndDroneDoesNotExist() {
        String droneId = "001";

        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(droneService.retrieveDroneById(droneId))
                .expectErrorSatisfies(response -> assertThat(response)
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage(String.format(Message.DRONE_DOES_NOT_EXIST, droneId)))
                .verify();

        verify(droneRepository, times(1)).findDroneById(anyString());
        verify(medicationService, never()).retrieveAllMedicationsByIdIn(anySet());
    }

    @Test
    void shouldLoadMedicationItemsOnDroneSuccessfully() {
        Drone drone = TestModels.drone("D-001", 60.0, 70);

        Medication med1 = TestModels.medication("M-001", 35.0);
        Medication med2 = TestModels.medication("M-002", 20.0);
        Set<String> medIds = Set.of(med1.getId(), med2.getId());

        MedicationResponseDTO medResponse1 = TestModels.medicationResponseDTO(med1);
        MedicationResponseDTO medResponse2 = TestModels.medicationResponseDTO(med2);


        LoadMedicationItemsRequestDTO loadRequest = LoadMedicationItemsRequestDTO.builder()
                .droneId(drone.getId())
                .medicationIds(medIds)
                .build();

        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.just(drone));
        when(medicationService.retrieveAllUnloadedMedicationsByIdIn(anySet())).thenReturn(Mono.just(List.of(medResponse1, medResponse2)));
        when(droneRepository.save(any(Drone.class))).thenReturn(Mono.just(drone));
        when(medicationService.updateLoadedStatusForMedicationsWithIds(anySet())).thenReturn(Mono.empty());

        StepVerifier.create(droneService.loadMedicationItems(loadRequest))
                .expectNext()
                .verifyComplete();

        assertEquals(2, drone.getMedicationIds().size());
        assertEquals(State.LOADED, drone.getState());
        assertEquals(65, drone.getBatteryCapacity());

        verify(droneRepository, times(1)).findDroneById(anyString());
        verify(medicationService, times(1)).retrieveAllUnloadedMedicationsByIdIn(anySet());
        verify(droneRepository, times(1)).save(any(Drone.class));
        verify(medicationService, times(1)).updateLoadedStatusForMedicationsWithIds(anySet());
    }

    @Test
    void shouldThrowInvalidRequestExceptionWhenLoadingMedicationItemsAndDroneBatteryIsLow() {
        Drone drone = TestModels.drone("D-001", 60.0, 24);

        LoadMedicationItemsRequestDTO loadRequest = LoadMedicationItemsRequestDTO.builder()
                .droneId(drone.getId())
                .medicationIds(Set.of("Med-001"))
                .build();

        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.just(drone));

        StepVerifier.create(droneService.loadMedicationItems(loadRequest))
                .expectErrorSatisfies(response -> assertThat(response)
                        .isInstanceOf(InvalidRequestException.class)
                        .hasMessage(Message.LOW_BATTERY))
                .verify();

        verify(droneRepository, times(1)).findDroneById(anyString());
        verify(medicationService, never()).retrieveAllMedicationsByIdIn(anySet());
        verify(droneRepository, never()).save(any(Drone.class));
        verify(medicationService, never()).updateLoadedStatusForMedicationsWithIds(anySet());
    }

    @Test
    void shouldThrowInvalidRequestExceptionWhenLoadingMedicationItemsForDroneWithNoExistingItemAndItemsWeightIsGreaterThanDroneWeightLimit() {
        Drone drone = TestModels.drone("D-001", 60.0, 70);

        Medication med1 = TestModels.medication("M-001", 40.0);
        Medication med2 = TestModels.medication("M-002", 30.0);
        Set<String> medIds = Set.of(med1.getId(), med2.getId());

        MedicationResponseDTO medResponse1 = TestModels.medicationResponseDTO(med1);
        MedicationResponseDTO medResponse2 = TestModels.medicationResponseDTO(med2);


        LoadMedicationItemsRequestDTO loadRequest = LoadMedicationItemsRequestDTO.builder()
                .droneId(drone.getId())
                .medicationIds(medIds)
                .build();

        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.just(drone));
        when(medicationService.retrieveAllUnloadedMedicationsByIdIn(anySet())).thenReturn(Mono.just(List.of(medResponse1, medResponse2)));

        double requestItemsWeight = med1.getWeight() + med2.getWeight();

        StepVerifier.create(droneService.loadMedicationItems(loadRequest))
                .expectErrorSatisfies(response -> assertThat(response)
                        .isInstanceOf(InvalidRequestException.class)
                        .hasMessage(String.format(Message.WEIGHT_LIMIT_EXCEEDED, Double.sum(requestItemsWeight, -(drone.getWeightLimit())))))
                .verify();

        verify(droneRepository, times(1)).findDroneById(anyString());
        verify(medicationService, times(1)).retrieveAllUnloadedMedicationsByIdIn(anySet());
        verify(droneRepository, never()).save(any(Drone.class));
        verify(medicationService, never()).updateLoadedStatusForMedicationsWithIds(anySet());
    }

    @Test
    void shouldThrowInvalidRequestExceptionWhenLoadingMedicationItemsForDroneWithExistingItemsWeightAndRequestItemsWeightIsGreaterThanDroneWeightLimit() {

        Medication med1 = TestModels.medication("M-001", 40.0);
        Medication med2 = TestModels.medication("M-002", 30.0);
        Set<String> requestMedIds = Set.of(med1.getId(), med2.getId());

        Medication med3 = TestModels.medication("M-003", 35.0);
        Medication med4 = TestModels.medication("M-004", 20.0);
        Set<String> existingMedIds = Set.of(med3.getId(), med4.getId());


        MedicationResponseDTO medResponse1 = TestModels.medicationResponseDTO(med1);
        MedicationResponseDTO medResponse2 = TestModels.medicationResponseDTO(med2);

        MedicationResponseDTO medResponse3 = TestModels.medicationResponseDTO(med3);
        MedicationResponseDTO medResponse4 = TestModels.medicationResponseDTO(med4);

        Drone drone = TestModels.drone("D-001", 60.0, 100);
        drone.getMedicationIds().addAll(existingMedIds);

        LoadMedicationItemsRequestDTO loadRequest = LoadMedicationItemsRequestDTO.builder()
                .droneId(drone.getId())
                .medicationIds(requestMedIds)
                .build();

        double requestItemsWeight = med1.getWeight() + med2.getWeight();
        double existingItemsWeight = med3.getWeight() + med4.getWeight();
        double totalPotentialWeight = requestItemsWeight + existingItemsWeight;


        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.just(drone));
        when(medicationService.retrieveAllUnloadedMedicationsByIdIn(anySet())).thenReturn(Mono.just(List.of(medResponse1, medResponse2)));
        when(medicationService.retrieveAllMedicationsByIdIn(anySet())).thenReturn(Mono.just(List.of(medResponse3, medResponse4)));

        StepVerifier.create(droneService.loadMedicationItems(loadRequest))
                .expectErrorSatisfies(response -> assertThat(response)
                        .isInstanceOf(InvalidRequestException.class)
                        .hasMessage(String.format(Message.WEIGHT_LIMIT_EXCEEDED, Double.sum(totalPotentialWeight, -(drone.getWeightLimit())))))
                .verify();

        verify(droneRepository, times(1)).findDroneById(anyString());
        verify(medicationService, times(1)).retrieveAllUnloadedMedicationsByIdIn(anySet());
        verify(medicationService, times(1)).retrieveAllMedicationsByIdIn(anySet());
        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    void shouldRetrieveAvailableDronesForLoadingSuccessfully() {
        Drone drone1 = TestModels.drone("D-001", 30.0, 50);
        drone1.setState(State.IDLE);

        Drone drone2 = TestModels.drone("D-002", 80.0, 70);
        drone2.setState(State.LOADING);

        Drone drone3 = TestModels.drone("D-003", 80.0, 70);
        drone3.setState(State.LOADED);

        Drone drone4 = TestModels.drone("D-004", 80.0, 20);
        drone4.setState(State.IDLE);

        when(droneRepository.findAllByStateInAndBatteryCapacityGreaterThanEqual(anyList(), anyInt())).thenReturn(Flux.just(drone1, drone2));
        when(medicationService.retrieveAllMedicationsByIdIn(anySet())).thenReturn(Mono.just(Collections.emptyList()));

        DroneResponseDTO expectedDroneResponseDTO1 = DroneMapper.mapToDroneResponseDTO(drone1, Collections.emptyList());
        DroneResponseDTO expectedDroneResponseDTO2 = DroneMapper.mapToDroneResponseDTO(drone2, Collections.emptyList());

        StepVerifier.create(droneService.retrieveAvailableDronesForLoading())
                .expectNextMatches(response -> {
                    assertEquals(2, response.size());
                    assertEquals(expectedDroneResponseDTO1, response.getFirst());
                    assertEquals(expectedDroneResponseDTO2, response.getLast());
                    return true;
                })
                .verifyComplete();

        verify(droneRepository, times(1)).findAllByStateInAndBatteryCapacityGreaterThanEqual(anyList(), anyInt());
        verify(medicationService, times(2)).retrieveAllMedicationsByIdIn(anySet());
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void shouldRetrieveDronesByStateSuccessfully(State state) {

        Drone drone = TestModels.drone("D-001", 30.0, 50);
        drone.setState(state);

        when(droneRepository.findAllByStateIs(state)).thenReturn(Flux.just(drone));
        when(medicationService.retrieveAllMedicationsByIdIn(anySet())).thenReturn(Mono.just(Collections.emptyList()));

        DroneResponseDTO expectedDroneResponseDTO = DroneMapper.mapToDroneResponseDTO(drone, Collections.emptyList());

        StepVerifier.create(droneService.retrieveDronesByState(state.name()))
                .expectNextMatches(response -> {
                    assertEquals(1, response.size());
                    assertEquals(expectedDroneResponseDTO, response.getFirst());
                    return true;
                })
                .verifyComplete();

        verify(droneRepository, times(1)).findAllByStateIs(state);
        verify(medicationService, times(1)).retrieveAllMedicationsByIdIn(anySet());
    }

    @Test
    void shouldThrowInvalidRequestExceptionWhenRetrievingDronesByStateAndStateIsNotValid() {
        String state = "SomeState";

        StepVerifier.create(droneService.retrieveDronesByState(state))
                .expectErrorSatisfies(response -> assertThat(response)
                        .isInstanceOf(InvalidRequestException.class)
                        .hasMessage(String.format(Message.INVALID_STATE, state, Arrays.toString(State.values()))))
                .verify();
    }

    @Test
    void shouldRetrieveDroneBatteryLevelSuccessfully(){
        Drone drone = TestModels.drone("D-001", 30.0, 50);

        when(droneRepository.findDroneById(anyString())).thenReturn(Mono.just(drone));

        StepVerifier.create(droneService.retrieveDroneBatteryLevel(drone.getId()))
                .expectNextMatches(response -> {
                    assertEquals(State.IDLE, response.state());
                    assertEquals(drone.getBatteryCapacity(), response.batteryLevel());
                    return true;
                })
                .verifyComplete();

        verify(droneRepository, times(1)).findDroneById(anyString());

    }

}
