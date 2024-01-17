package com.musalasoft.drones.repositories;

import com.musalasoft.drones.models.Medication;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface MedicationRepository extends ReactiveMongoRepository<Medication, String> {
    Flux<Medication> findAllByIdIn(Set<String> medicationIds);
    Flux<Medication> findAllByIdInAndLoadedIs(Set<String> medicationIds, boolean loaded);
    Mono<Boolean> existsMedicationByCode(String code);
    Flux<Medication> findAllByLoadedIs(boolean loaded);
}
