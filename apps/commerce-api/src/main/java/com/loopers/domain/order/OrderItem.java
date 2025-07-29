package com.loopers.domain.order;

import lombok.Getter;

@Getter
public class OrderItem {
    private Long id;
    private Long productId;
    private Long productOptionId;

    public OrderItem(Long productId, Long productOptionId) {
        this.productId = productId;
        this.productOptionId = productOptionId;
    }
}
