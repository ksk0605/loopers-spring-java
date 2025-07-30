package com.loopers.application.order;

import com.loopers.domain.order.OrderItem;

public record OrderItemInfo(
    Long productId,
    Long productOptionId,
    Integer quantity
) {
    public static OrderItemInfo of(OrderItem item) {
        return new OrderItemInfo(item.getProductId(), item.getProductOptionId(), item.getQuantity());
    }
}
