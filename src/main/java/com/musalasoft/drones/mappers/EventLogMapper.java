package com.musalasoft.drones.mappers;

import com.musalasoft.drones.dtos.EventLogResponseDTO;
import com.musalasoft.drones.models.EventLog;
import com.musalasoft.drones.utils.DateUtil;

public interface EventLogMapper {
    static EventLogResponseDTO mapToEventLogResponseDTO(EventLog eventLog){
        return EventLogResponseDTO.builder()
                .droneId(eventLog.getDroneId())
                .batteryCapacity(eventLog.getBatteryCapacity())
                .state(eventLog.getState())
                .weightLimit(eventLog.getWeightLimit())
                .loadedItemsWeight(eventLog.getLoadedItemsWeight())
                .createdAt(DateUtil.formatLocalDateTime(eventLog.getCreatedAt()))
                .build();
    }
}
