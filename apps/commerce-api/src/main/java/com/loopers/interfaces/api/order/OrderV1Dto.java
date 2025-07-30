package com.loopers.interfaces.api.order;

import java.time.LocalDateTime;
import java.util.List;

import com.loopers.application.order.OrderInfo;
import com.loopers.application.order.OrderItemInfo;

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
        public static OrderItemResponse from(OrderItemInfo orderItemInfo) {
            return new OrderItemResponse(
                orderItemInfo.productId(),
                orderItemInfo.productOptionId(),
                orderItemInfo.quantity()
            );
        }
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
        public static OrderResponse from(OrderInfo orderInfo) {
            return new OrderResponse(
                orderInfo.id(),
                orderInfo.items().stream().map(OrderItemResponse::from).toList(),
                orderInfo.userId(),
                orderInfo.status().name(),
                orderInfo.paymentMethod().name(),
                orderInfo.paymentStatus().name(),
                orderInfo.orderDate(),
                orderInfo.totalPrice().longValue()
            );
        }
    }
}
