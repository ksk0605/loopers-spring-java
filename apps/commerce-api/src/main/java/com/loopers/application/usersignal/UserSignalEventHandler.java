package com.loopers.application.usersignal;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.UnlikeEvent;
import com.loopers.domain.product.ProductViewedEvent;
import com.loopers.domain.usersignal.TargetType;
import com.loopers.domain.usersignal.UserSignalService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSignalEventHandler {
    private final UserSignalService userSignalService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleLikeEvent(LikeEvent event) {
        userSignalService.updateLikeCount(event.getType(), event.getTargetId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUnlikeEvent(UnlikeEvent event) {
        userSignalService.updateLikeCount(event.getType(), event.getTargetId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductViewed(ProductViewedEvent event) {
        userSignalService.increaseViews(TargetType.PRODUCT, event.getProductId());
    }
}
