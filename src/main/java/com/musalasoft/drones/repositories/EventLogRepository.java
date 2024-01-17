package com.musalasoft.drones.repositories;

import com.musalasoft.drones.models.EventLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends ReactiveMongoRepository<EventLog, String>, EventLogRepositoryCustom {
}
