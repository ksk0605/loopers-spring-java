package com.loopers.domain.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderInfo(
    Long id,
    Long userId,
    List<OrderItemInfo> items,
    OrderStatus status,
    LocalDateTime orderDate
) {
    public static OrderInfo from(Order order) {
        return new OrderInfo(
            order.getId(),
            order.getUserId(),
            order.getItems().stream().map(OrderItemInfo::from).toList(),
            order.getStatus(),
            order.getOrderDate()
        );
    }
}
