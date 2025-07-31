package com.loopers.domain.order;

import java.util.List;

public class OrderCommand {
    public record Place(
        Long userId,
        List<OrderOption> options
    ) {
    }

    public record OrderOption(
        Long productId,
        Long productOptionId,
        Integer quantity
    ) {
        public OrderItem toOrderItem() {
            return new OrderItem(productId, productOptionId, quantity);
        }
    }
}
