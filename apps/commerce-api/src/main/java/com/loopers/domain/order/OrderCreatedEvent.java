package com.loopers.domain.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.loopers.domain.inventory.InventoryCommand.Option;
import com.loopers.domain.event.InternalEvent;
import com.loopers.domain.event.Loggable;

import lombok.Getter;

@Getter
public class OrderCreatedEvent implements Loggable {
    private Order order;
    private Long couponId;
    private String userName;

    public OrderCreatedEvent(Order order, Long couponId, String userName) {
        this.order = order;
        this.couponId = couponId;
        this.userName = userName;
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

    public String getOrderId() {
        return order.getOrderId();
    }

    @Override
    public InternalEvent toInternalEvent() {
        return new InternalEvent(this.getClass().getSimpleName(), Map.of(
            "orderId", this.getOrderId(),
            "userId", this.getUserId(),
            "totalPrice", this.getTotalPrice(),
            "items", this.getItems(),
            "couponId", this.getCouponId(),
            "userName", this.getUserName()
        ));
    }
}
