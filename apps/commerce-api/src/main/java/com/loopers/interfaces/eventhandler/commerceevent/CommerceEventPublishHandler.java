package com.loopers.interfaces.eventhandler.commerceevent;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.commerceevent.CommerceEventMessageSender;
import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.UnlikeEvent;
import com.loopers.domain.product.ProductViewedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommerceEventPublishHandler {
    private final CommerceEventMessageSender commerceEventMessageSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(LikeEvent event) {
        commerceEventMessageSender.sendUserSignalsEvent(event.toSendCommand());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UnlikeEvent event) {
        commerceEventMessageSender.sendUserSignalsEvent(event.toSendCommand());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductViewedEvent event) {
        commerceEventMessageSender.sendUserSignalsEvent(event.toSendCommand());
    }
}
