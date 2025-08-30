package com.loopers.application.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.event.InternalEventService;
import com.loopers.domain.event.Loggable;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoggableEventHandler {
    private final InternalEventService internalEventService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(Loggable loggable) {
        internalEventService.log(loggable);
    }
}
