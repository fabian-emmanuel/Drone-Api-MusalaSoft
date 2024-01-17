package com.musalasoft.drones.controllers;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.dtos.BaseResponse;
import com.musalasoft.drones.enums.State;
import com.musalasoft.drones.services.EventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/event-logs")
public class EventLogController {

    private final EventLogService eventLogService;

    @GetMapping
    public Mono<BaseResponse<?>> retrieveEventLogs(@RequestParam(required = false) Long page,
                                                   @RequestParam(required = false) Long size,
                                                   @RequestParam(required = false) State state){

        return eventLogService.retrieveEventLogs(page, size, state)
                .map(eventLogs -> new BaseResponse<>(true, HttpStatus.OK, Message.EVENT_LOGS_RETRIEVED_SUCCESSFULLY, eventLogs));
    }
}
