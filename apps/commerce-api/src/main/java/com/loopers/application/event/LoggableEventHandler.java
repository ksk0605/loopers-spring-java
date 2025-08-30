package com.loopers.application.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.loopers.domain.event.InternalEventService;
import com.loopers.domain.event.Loggable;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoggableEventHandler {
    private final InternalEventService internalEventService;

    @Async
    @EventListener
    public void handle(Loggable loggable) {
        internalEventService.log(loggable);
    }
}
