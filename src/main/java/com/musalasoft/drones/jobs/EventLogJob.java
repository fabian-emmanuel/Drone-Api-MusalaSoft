package com.musalasoft.drones.jobs;

import com.musalasoft.drones.dtos.MedicationResponseDTO;
import com.musalasoft.drones.models.EventLog;
import com.musalasoft.drones.services.DroneService;
import com.musalasoft.drones.services.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Mono;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class EventLogJob {

    private final DroneService droneService;
    private final EventLogService eventLogService;


    @Scheduled(fixedRateString = "${event-schedule-rate}")
    public void checkBatterLevelAndLogEvent() {
        droneService.retrieveAllDrones()
                .flatMap(droneResponseDTO -> {

                    double loadedItemsWeight = droneResponseDTO.medications().stream().mapToDouble(MedicationResponseDTO::weight).sum();

                    EventLog eventLog = EventLog.builder()
                            .droneId(droneResponseDTO.id())
                            .batteryCapacity(droneResponseDTO.batteryCapacity())
                            .state(droneResponseDTO.state())
                            .weightLimit(droneResponseDTO.weightLimit())
                            .loadedItemsWeight(loadedItemsWeight)
                            .build();

                    return Mono.just(eventLog);
                })
                .collectList()
                .flatMap(eventLogService::saveAll)
                .subscribe();
    }

}
