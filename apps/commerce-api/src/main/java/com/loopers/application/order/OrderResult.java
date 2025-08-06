package com.loopers.application.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.loopers.domain.coupon.UserCoupon;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderItem;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentStatus;

public record OrderResult(
    Long id,
    Long userId,
    OrderStatus status,
    LocalDateTime orderDate,
    List<OrderItemResult> items,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    BigDecimal totalPrice
) {
    public static OrderResult of(Order order, Payment payment) {
        return new OrderResult(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getOrderDate(),
            order.getItems().stream().map(OrderItemResult::from).toList(),
            payment.getMethod(),
            payment.getStatus(),
            order.getTotalPrice()
        );
    }

    public static OrderResult of(Order order, Payment payment, UserCoupon userCoupon) {
        return new OrderResult(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getOrderDate(),
            order.getItems().stream().map(OrderItemResult::from).toList(),
            payment.getMethod(),
            payment.getStatus(),
            order.getTotalPrice().subtract(userCoupon.getDiscountAmount())
        );
    }

    public record OrderItemResult(
        Long productId,
        Long productOptionId,
        Integer quantity
    ) {
        public static OrderItemResult from(OrderItem item) {
            return new OrderItemResult(item.getProductId(), item.getProductOptionId(), item.getQuantity());
        }
    }
}
