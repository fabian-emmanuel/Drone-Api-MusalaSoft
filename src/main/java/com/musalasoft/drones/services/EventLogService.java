package com.musalasoft.drones.services;

import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.models.EventLog;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface EventLogService {
    Mono<Void> saveAll(List<EventLog> eventLogs);
    Mono<Map<String, Object>> retrieveEventLogs(Long page, Long size, State state);
}
