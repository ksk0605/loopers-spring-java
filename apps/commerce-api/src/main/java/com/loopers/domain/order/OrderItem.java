package com.loopers.domain.order;

import lombok.Getter;

@Getter
public class OrderItem {
    private Long id;
    private Long productId;
    private Long productOptionId;
    private Integer quantity;

    public OrderItem(Long productId, Long productOptionId, Integer quantity) {
        this.productId = productId;
        this.productOptionId = productOptionId;
        this.quantity = quantity;
    }
}
