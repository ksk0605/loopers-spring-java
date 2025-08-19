package com.loopers.application.order;

import java.util.List;

import com.loopers.domain.order.Order;

public record OrderResults(
    List<OrderResult> results
) {
    public static OrderResults of(List<Order> orders) {
        List<OrderResult> orderResults = orders.stream()
            .map(order -> OrderResult.of(order))
            .toList();
        return new OrderResults(orderResults);
    }
}
