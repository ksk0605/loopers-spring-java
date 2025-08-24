package com.loopers.domain.order;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publishOrderPaid(Order order) {
        publisher.publishEvent(OrderPaidEvent.from(order));
    }
}
