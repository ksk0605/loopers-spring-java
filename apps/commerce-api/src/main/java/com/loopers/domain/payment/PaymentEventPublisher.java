package com.loopers.domain.payment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publishSuccess(String orderId) {
        publisher.publishEvent(new PaymentSuccessEvent(orderId));
    }
}
