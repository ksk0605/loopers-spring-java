package com.loopers.domain.product;

import java.util.Map;

import com.loopers.domain.event.InternalEvent;
import com.loopers.domain.event.Loggable;

import lombok.Getter;

@Getter
public class ProductViewedEvent implements Loggable {
    private final Long productId;

    public ProductViewedEvent(Long productId) {
        this.productId = productId;
    }

    @Override
    public InternalEvent toInternalEvent() {
        return new InternalEvent(
            this.getClass().getSimpleName(),
            Map.of(
                "productId", this.productId
            )
        );
    }
}
