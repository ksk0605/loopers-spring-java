package com.loopers.application.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.loopers.domain.coupon.CouponUsage;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderStatus;

public record OrderResult(
    Long id,
    Long userId,
    OrderStatus status,
    LocalDateTime orderDate,
    String orderId,
    List<OrderItemResult> items,
    BigDecimal totalPrice) {
    public static OrderResult of(Order order) {
        return new OrderResult(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getOrderDate(),
            order.getOrderId(),
            order.getItems().stream().map(OrderItemResult::from).toList(),
            order.getTotalPrice());
    }

    public static OrderResult of(Order order, CouponUsage userCoupon) {
        return new OrderResult(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getOrderDate(),
            order.getOrderId(),
            order.getItems().stream().map(OrderItemResult::from).toList(),
            order.getTotalPrice().subtract(userCoupon.getDiscountAmount()));
    }

    public record OrderItemResult(
        Long productId,
        Long productOptionId,
        Integer quantity) {
        public static OrderItemResult from(OrderItem item) {
            return new OrderItemResult(item.getProductId(), item.getProductOptionId(), item.getQuantity());
        }
    }
}
