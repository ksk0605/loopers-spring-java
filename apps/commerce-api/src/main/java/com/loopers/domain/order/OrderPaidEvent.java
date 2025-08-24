package com.loopers.domain.order;

import java.util.List;

import lombok.Getter;

@Getter
public class OrderPaidEvent {
    private Long orderId;
    private List<Item> items;

    public OrderPaidEvent(Long orderId, List<Item> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public static OrderPaidEvent from(Order order) {
        return new OrderPaidEvent(
            order.getId(),
            order.getItems().stream()
                .map(orderItem -> new Item(
                    orderItem.getProductId(),
                    orderItem.getProductOptionId(),
                    orderItem.getQuantity()))
                .toList()
        );
    }

    public record Item(Long productId, Long productOptionId, Integer quantity) {
    }
}
