package com.loopers.application.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderItemInfo;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentStatus;

public record OrderResult(
    Long id,
    Long userId,
    OrderStatus status,
    LocalDateTime orderDate,
    List<OrderItemInfo> items,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    BigDecimal totalPrice
) {
    public static OrderResult of(OrderInfo orderInfo, PaymentInfo paymentInfo, BigDecimal totalPrice) {
        return new OrderResult(
            orderInfo.id(),
            orderInfo.userId(),
            orderInfo.status(),
            orderInfo.orderDate(),
            orderInfo.items(),
            paymentInfo.method(),
            paymentInfo.status(),
            totalPrice);
    }
}
