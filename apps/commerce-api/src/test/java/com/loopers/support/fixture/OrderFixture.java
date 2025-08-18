package com.loopers.support.fixture;

import static com.loopers.support.fixture.OrderItemFixture.anOrderItem;

import java.util.List;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderItem;

public class OrderFixture {
    private Long userId = 1L;
    private List<OrderItem> orderItems = List.of(
        anOrderItem()
            .productId(1L)
            .productOptionId(1L)
            .quantity(1)
            .build());

    public static OrderFixture anOrder() {
        return new OrderFixture();
    }

    public OrderFixture userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public OrderFixture orderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Order build() {
        return new Order(
            userId,
            orderItems
        );
    }

    private OrderFixture() {
    }
}
