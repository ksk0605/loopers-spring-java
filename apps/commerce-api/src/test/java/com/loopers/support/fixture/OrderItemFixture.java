package com.loopers.support.fixture;

import java.math.BigDecimal;

import com.loopers.domain.order.OrderItem;

public class OrderItemFixture {
    private Long productId = 1L;
    private Long productOptionId = 1L;
    private Integer quantity = 1;
    private BigDecimal basePrice = BigDecimal.valueOf(10000);
    private BigDecimal optionPrice = BigDecimal.valueOf(1000);

    public static OrderItemFixture anOrderItem() {
        return new OrderItemFixture();
    }

    public OrderItemFixture productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public OrderItemFixture productOptionId(Long productOptionId) {
        this.productOptionId = productOptionId;
        return this;
    }

    public OrderItemFixture quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderItemFixture basePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    public OrderItemFixture optionPrice(BigDecimal optionPrice) {
        this.optionPrice = optionPrice;
        return this;
    }

    public OrderItem build() {
        return new OrderItem(
            productOptionId,
            productId,
            quantity,
            basePrice,
            optionPrice
        );
    }

    private OrderItemFixture() {
    }
}
