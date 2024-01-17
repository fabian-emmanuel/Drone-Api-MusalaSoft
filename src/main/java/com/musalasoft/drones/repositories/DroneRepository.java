package com.musalasoft.drones.repositories;

import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.models.Drone;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface DroneRepository extends ReactiveMongoRepository<Drone, String> {
    Mono<Drone> findDroneById(String id);
    Flux<Drone> findAllByStateInAndBatteryCapacityGreaterThanEqual(List<State> states, int batteryCapacity);
    Flux<Drone> findAllByStateIs(State state);
}
