package com.loopers.application.coupon;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponEventHandler {
    private final CouponService couponService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        couponService.apply(orderCreatedEvent.getUserId(), orderCreatedEvent.getCouponId(),
                orderCreatedEvent.getTotalPrice());
    }
}
