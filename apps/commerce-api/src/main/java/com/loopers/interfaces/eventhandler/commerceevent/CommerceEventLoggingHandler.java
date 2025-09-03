package com.loopers.interfaces.eventhandler.commerceevent;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.commerceevent.CommerceEventService;
import com.loopers.domain.like.LikeEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommerceEventLoggingHandler {
    private final CommerceEventService commerceEventService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleLikeEvent(LikeEvent event) {
        commerceEventService.log(event.toLogCommand());
    }
}
