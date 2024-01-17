package com.musalasoft.drones.repositories.impl;

import com.musalasoft.drones.dtos.EventLogResponseDTO;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.mappers.EventLogMapper;
import com.musalasoft.drones.models.EventLog;
import com.musalasoft.drones.repositories.EventLogRepositoryCustom;
import com.musalasoft.drones.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class EventLogRepositoryCustomImpl implements EventLogRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<Map<String, Object>> retrieveAllEventLogs(Long page, Long size, State state) {
        PageRequest pageRequest = PageUtil.buildPageRequest(page, size);

        Criteria criteria = new Criteria();

        if (Objects.nonNull(state)) {
            criteria.and("state").is(state);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.ASC, "created_at"),
                Aggregation.skip((long) pageRequest.getPageNumber() * pageRequest.getPageSize()),
                Aggregation.limit(pageRequest.getPageSize())
        );

        return mongoTemplate.aggregate(aggregation, EventLog.class, EventLog.class)
                .map(EventLogMapper::mapToEventLogResponseDTO)
                .collectList()
                .flatMap(data -> mongoTemplate.count(Query.query(criteria), EventLog.class)
                        .map(total -> {
                            PageImpl<EventLogResponseDTO> eventLogResponseDTOPage = new PageImpl<>(data, pageRequest, total);
                            return PageUtil.buildPaginatedResponse(eventLogResponseDTOPage, pageRequest, total, "EventLogs");
                        })
                );

    }

}
