package com.loopers.application.order;

import java.util.List;

import com.loopers.domain.order.Order;
import com.loopers.domain.payment.Payment;

public record OrderResults(
    List<OrderResult> results
) {
    public static OrderResults of(List<Order> orders, List<Payment> payments) {
        List<OrderResult> orderResults = orders.stream()
            .map(order -> OrderResult.of(order, payments.stream()
                .filter(payment -> payment.getOrderId().equals(order.getId()))
                .findFirst()
                .get()))
            .toList();
        return new OrderResults(orderResults);
    }
}
