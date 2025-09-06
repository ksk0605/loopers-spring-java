package com.loopers.domain.order;

import static com.loopers.support.util.RequireUtils.requireNonEmpty;
import static com.loopers.support.util.RequireUtils.requireNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.loopers.domain.BaseEntity;
import com.loopers.support.util.IdempotencyCreator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    private Long userId;

    private String orderId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private OrderStatus status;

    private BigDecimal orderAmount;

    private LocalDateTime orderDate;

    public Order(Long userId, List<OrderItem> items) {
        this.userId = requireNotNull(userId, "주문 생성은 유저 ID가 필수입니다.");
        this.items = requireNonEmpty(items, "주문 생성은 주문 아이템이 필수입니다.");
        this.status = OrderStatus.PENDING;
        this.orderId = IdempotencyCreator.create(this);
        this.orderAmount = getTotalPrice();
    }

    public static Order from(OrderCommand.Order command) {
        List<OrderItem> orderItems = command.options().stream()
            .map(OrderCommand.OrderOption::toOrderItem)
            .toList();
        return new Order(command.userId(), orderItems);
    }

    public void place(OrderValidator validator) {
        validator.validateOrder(this);
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void completePayment() {
        if (this.status == OrderStatus.PAYMENT_COMPLETED) {
            throw new IllegalStateException("이미 결제된 주문입니다.");
        }
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
            .map(item -> item.calculatePrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void applyDiscount(BigDecimal discountAmount) {
        this.orderAmount = getTotalPrice().subtract(discountAmount);
    }
}
