package com.loopers.application.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentStatus;

public record OrderInfo(
    Long id,
    Long userId,
    OrderStatus status,
    LocalDateTime orderDate,
    List<OrderItemInfo> items,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    BigDecimal totalPrice
) {
    public static OrderInfo of(Order order, Payment payment, BigDecimal totalPrice) {
        return new OrderInfo(
            order.getId(), 
            order.getUserId(), 
            order.getStatus(), 
            order.getOrderDate(), 
            order.getItems().stream().map(OrderItemInfo::of).toList(), 
            payment.getMethod(), 
            payment.getStatus(), 
            totalPrice
        );
    }
}
