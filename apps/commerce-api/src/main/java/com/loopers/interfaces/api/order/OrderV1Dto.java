package com.loopers.interfaces.api.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderV1Dto {

    public record OrderItemRequest(
        Long productId,
        Long productOptionId,
        Integer quantity
    ) {
    }

    public record OrderRequest(
        List<OrderItemRequest> items
    ) {
    }

    public record OrderItemResponse(
        Long productId,
        Long productOptionId,
        Integer quantity
    ) {
    }

    public record OrderResponse(
        Long id,
        List<OrderItemResponse> items,
        Long userId,
        String orderStatus,
        String paymentMethod,
        String paymentStatus,
        LocalDateTime orderDate,
        Long totalPrice
    ) {
    }
}
