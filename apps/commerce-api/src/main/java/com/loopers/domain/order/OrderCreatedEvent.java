package com.loopers.domain.order;

import java.math.BigDecimal;
import java.util.List;

import com.loopers.domain.inventory.InventoryCommand.Option;

import lombok.Getter;

@Getter
public class OrderCreatedEvent {
    private Order order;
    private Long couponId;

    public OrderCreatedEvent(Order order, Long couponId) {
        this.order = order;
        this.couponId = couponId;
    }

    public List<Option> getItems() {
        return order.getItems().stream()
            .map(item -> new Option(item.getProductId(), item.getProductOptionId(), item.getQuantity()))
            .toList();
    }

    public Long getUserId() {
        return order.getUserId();
    }

    public BigDecimal getTotalPrice() {
        return order.getTotalPrice();
    }
}
