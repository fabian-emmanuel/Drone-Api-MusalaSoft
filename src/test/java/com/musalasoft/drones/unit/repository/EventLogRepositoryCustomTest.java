package com.musalasoft.drones.unit.repository;

import com.musalasoft.drones.Utils.TestModels;
import com.musalasoft.drones.dtos.EventLogResponseDTO;
import com.musalasoft.drones.models.EventLog;
import com.musalasoft.drones.repositories.impl.EventLogRepositoryCustomImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EventLogRepositoryCustomTest {

    @InjectMocks
    EventLogRepositoryCustomImpl eventLogRepositoryCustom;
    @Mock
    ReactiveMongoTemplate mongoTemplate;

    @Test
    void shouldRetrieveAllEventLogsSuccessfully() {
        EventLog log1 = TestModels.eventLog();
        EventLog log2 = TestModels.eventLog();
        EventLog log3 = TestModels.eventLog();
        EventLog log4 = TestModels.eventLog();
        EventLog log5 = TestModels.eventLog();

        EventLogResponseDTO responseDTO1 = TestModels.eventLogResponseDTO(log1);
        EventLogResponseDTO responseDTO2 = TestModels.eventLogResponseDTO(log2);
        EventLogResponseDTO responseDTO3 = TestModels.eventLogResponseDTO(log3);
        EventLogResponseDTO responseDTO4 = TestModels.eventLogResponseDTO(log4);
        EventLogResponseDTO responseDTO5 = TestModels.eventLogResponseDTO(log5);

        when(mongoTemplate.aggregate(any(Aggregation.class), eq(EventLog.class), eq(EventLog.class))).thenReturn(Flux.just(log1, log2, log3, log4, log5));
        when(mongoTemplate.count(any(Query.class), eq(EventLog.class))).thenReturn(Mono.just(5L));

        StepVerifier.create(eventLogRepositoryCustom.retrieveAllEventLogs(1L, 10L, null))
                .expectNextMatches(response -> {
                    List<EventLogResponseDTO> responseDTOList = (List<EventLogResponseDTO>) response.get("EventLogs");
                    assertEquals(5, responseDTOList.size());
                    assertEquals(responseDTO1, responseDTOList.getFirst());
                    assertEquals(responseDTO2, responseDTOList.get(1));
                    assertEquals(responseDTO3, responseDTOList.get(2));
                    assertEquals(responseDTO4, responseDTOList.get(3));
                    assertEquals(responseDTO5, responseDTOList.getLast());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq(EventLog.class), eq(EventLog.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(EventLog.class));
    }
}
