package com.musalasoft.drones.unit.services;

import com.musalasoft.drones.Utils.TestModels;
import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.constants.Regex;
import com.musalasoft.drones.dtos.MedicationRequestDTO;
import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.exceptions.DuplicateException;
import com.musalasoft.drones.models.Medication;
import com.musalasoft.drones.repositories.MedicationRepository;
import com.musalasoft.drones.services.impl.MedicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static com.musalasoft.drones.utils.CustomUtil.FAKER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MedicationServiceTest {

    @InjectMocks
    MedicationServiceImpl medicationService;
    @Mock
    MedicationRepository medicationRepository;

    @Test
    void shouldRegisterMedicationSuccessfullyWhenRequestIsValid(){
        var request = MedicationRequestDTO.builder()
                .name(FAKER.regexify(Regex.MED_NAME))
                .weight(FAKER.number().randomDouble(2,1,450))
                .code(FAKER.regexify(Regex.MED_CODE))
                .imageUrl(FAKER.internet().image())
                .build();

        var medication = TestModels.medication(request);

        when(medicationRepository.existsMedicationByCode(anyString())).thenReturn(Mono.just(Boolean.FALSE));
        when(medicationRepository.save(any(Medication.class))).thenReturn(Mono.just(medication));

        StepVerifier.create(medicationService.registerMedication(request))
                .expectNextMatches(responseDTO -> {
                    assertNotNull(responseDTO.id());
                    assertNotNull(responseDTO.createdAt());
                    assertEquals(request.name(), responseDTO.name());
                    assertEquals(request.code(), responseDTO.code());
                    assertEquals(request.weight(), responseDTO.weight());
                    assertEquals(request.imageUrl(), responseDTO.imageUrl());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(medicationRepository, times(1)).existsMedicationByCode(anyString());
        verify(medicationRepository, times(1)).save(any(Medication.class));
    }

    @Test
    void shouldReturnDuplicateExceptionWhenRegisteringMedicationWithExistingCode(){
        var request = MedicationRequestDTO.builder()
                .name(FAKER.regexify(Regex.MED_NAME))
                .weight(FAKER.number().randomDouble(2,1,450))
                .code(FAKER.regexify(Regex.MED_CODE))
                .imageUrl(FAKER.internet().image())
                .build();

        when(medicationRepository.existsMedicationByCode(anyString())).thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(medicationService.registerMedication(request))
                .expectErrorSatisfies(response -> assertThat(response)
                        .isInstanceOf(DuplicateException.class)
                        .hasMessage(String.format(Message.MEDICATION_EXISTS, request.code())))
                .verify();

        verify(medicationRepository, times(1)).existsMedicationByCode(anyString());
        verify(medicationRepository, never()).save(any(Medication.class));
    }

    @Test
    void shouldRetrieveAllMedicationsSuccessfully(){

        Medication med1 = TestModels.medication("M-001", 40.0);
        Medication med2 = TestModels.medication("M-002", 30.0);
        Medication med3 = TestModels.medication("M-003", 35.0);
        Medication med4 = TestModels.medication("M-004", 20.0);

        MedicationResponseDTO medResponse1 = TestModels.medicationResponseDTO(med1);
        MedicationResponseDTO medResponse2 = TestModels.medicationResponseDTO(med2);
        MedicationResponseDTO medResponse3 = TestModels.medicationResponseDTO(med3);
        MedicationResponseDTO medResponse4 = TestModels.medicationResponseDTO(med4);

        when(medicationRepository.findAll()).thenReturn(Flux.just(med1,med2,med3,med4));

        StepVerifier.create(medicationService.retrieveAllMedications())
                .expectNextMatches(response -> {
                    assertEquals(4, response.size());
                    assertEquals(medResponse1, response.getFirst());
                    assertEquals(medResponse2, response.get(1));
                    assertEquals(medResponse3, response.get(2));
                    assertEquals(medResponse4, response.getLast());
                    return true;
                })
                .verifyComplete();

        verify(medicationRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateLoadedStatusForMedicationsSuccessfully(){

        Medication med1 = TestModels.medication("M-001", 40d);
        Medication med2 = TestModels.medication("M-002", 30d);
        Medication med3 = TestModels.medication("M-003", 35d);
        Medication med4 = TestModels.medication("M-004", 20d);
        Set<String> ids = Set.of(med1.getId(), med2.getId(), med3.getId(), med4.getId());

        when(medicationRepository.findAllByIdIn(anySet())).thenReturn(Flux.just(med1,med2,med3,med4));
        when(medicationRepository.saveAll(anyList())).thenReturn(Flux.just(med1,med2,med3,med4));

        StepVerifier.create(medicationService.updateLoadedStatusForMedicationsWithIds(ids))
                .expectNext()
                .verifyComplete();

        verify(medicationRepository, times(1)).findAllByIdIn(anySet());
        verify(medicationRepository, times(1)).saveAll(anyList());

        assertTrue(med1.isLoaded());
        assertTrue(med2.isLoaded());
        assertTrue(med3.isLoaded());
        assertTrue(med4.isLoaded());
    }

}
