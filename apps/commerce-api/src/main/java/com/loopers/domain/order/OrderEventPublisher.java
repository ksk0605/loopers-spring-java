package com.loopers.domain.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        publisher.publishEvent(orderCreatedEvent);
    }
}
