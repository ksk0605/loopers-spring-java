package com.loopers.interfaces.api.order;

import java.time.LocalDateTime;
import java.util.List;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderResult;
import com.loopers.application.order.OrderResults;

public class OrderV1Dto {

    public record OrderItemRequest(
        Long productId,
        Long productOptionId,
        Integer quantity) {
    }

    public record OrderRequest(
        List<OrderItemRequest> items,
        Long couponId
    ) {
        public OrderCriteria.Order toOrderCriteria(Long userId) {
            return new OrderCriteria.Order(
                userId,
                items.stream()
                    .map(item ->
                        new OrderCriteria.Item(item.productId(), item.productOptionId(), item.quantity()))
                    .toList(),
                couponId
            );
        }
    }

    public record OrderItemResponse(
        Long productId,
        Long productOptionId,
        Integer quantity) {
        public static OrderItemResponse from(OrderResult.OrderItemResult orderItemInfo) {
            return new OrderItemResponse(
                orderItemInfo.productId(),
                orderItemInfo.productOptionId(),
                orderItemInfo.quantity());
        }
    }

    public record OrderResponse(
        Long id,
        String orderId,
        List<OrderItemResponse> items,
        Long userId,
        String orderStatus,
        LocalDateTime orderDate,
        Long totalPrice) {
        public static OrderResponse from(OrderResult orderInfo) {
            return new OrderResponse(
                orderInfo.id(),
                orderInfo.orderId(),
                orderInfo.items().stream().map(OrderItemResponse::from).toList(),
                orderInfo.userId(),
                orderInfo.status().name(),
                orderInfo.orderDate(),
                orderInfo.totalPrice().longValue());
        }
    }

    public record OrderResponses(
        List<OrderResponse> orders) {
        public static OrderResponses from(OrderResults orderResults) {
            return new OrderResponses(
                orderResults.results().stream()
                    .map(OrderResponse::from)
                    .toList());
        }
    }
}
