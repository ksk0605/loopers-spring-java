package com.loopers.domain.order;

public record OrderItemInfo(
    Long productId,
    Long productOptionId,
    Integer quantity
) {
    public static OrderItemInfo from(OrderItem orderItem) {
        return new OrderItemInfo(
            orderItem.getProductId(),
            orderItem.getProductOptionId(),
            orderItem.getQuantity()
        );
    }
}
