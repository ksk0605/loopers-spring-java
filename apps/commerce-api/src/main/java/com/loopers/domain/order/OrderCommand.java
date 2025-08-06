package com.loopers.domain.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderCommand {
    public record Order(
        Long userId,
        List<OrderOption> options
    ) {
    }

    public record OrderOption(
        Long productId,
        Long productOptionId,
        Integer quantity,
        BigDecimal basePrice,
        BigDecimal optionPrice
    ) {
        public OrderItem toOrderItem() {
            return new OrderItem(productId, productOptionId, quantity, basePrice, optionPrice);
        }
    }
}
