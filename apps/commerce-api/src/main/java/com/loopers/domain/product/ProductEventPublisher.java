package com.loopers.domain.product;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ProductEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishProductViewed(Long productId) {
        ProductViewedEvent event = new ProductViewedEvent(productId);
        applicationEventPublisher.publishEvent(event);
    }
}
