package com.musalasoft.drones.services.impl;

import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.models.EventLog;
import com.musalasoft.drones.repositories.EventLogRepository;
import com.musalasoft.drones.services.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventLogServiceImpl implements EventLogService {

    private final EventLogRepository eventLogRepository;

    @Override
    public Mono<Void> saveAll(List<EventLog> eventLogs) {
        return eventLogRepository.saveAll(eventLogs).then();
    }

    @Override
    public Mono<Map<String, Object>> retrieveEventLogs(Long page, Long size, State state) {
        return eventLogRepository.retrieveAllEventLogs(page, size, state);
    }
}
