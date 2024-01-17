package com.musalasoft.drones.repositories;

import com.musalasoft.drones.enums.State;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface EventLogRepositoryCustom {
    Mono<Map<String, Object>> retrieveAllEventLogs(Long page, Long size, State state);
}
