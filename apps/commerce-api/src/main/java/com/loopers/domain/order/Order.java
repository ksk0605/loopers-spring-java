package com.loopers.domain.order;

import static com.loopers.support.util.RequireUtils.requireNonEmpty;
import static com.loopers.support.util.RequireUtils.requireNotNull;

import java.time.LocalDateTime;
import java.util.List;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    private OrderStatus status;

    private LocalDateTime orderDate;

    public Order(Long userId, List<OrderItem> items) {
        this.userId = requireNotNull(userId, "주문 생성은 유저 ID가 필수입니다.");
        this.items = requireNonEmpty(items, "주문 생성은 주문 아이템이 필수입니다.");
        this.status = OrderStatus.PENDING;
    }

    public void place() {
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public void pay() {
        if (this.status == OrderStatus.PAYMENT_COMPLETED) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 결제된 주문입니다.");
        }
        this.status = OrderStatus.PAYMENT_COMPLETED;
    }

    public boolean isPaid() {
        return this.status == OrderStatus.PAYMENT_COMPLETED;
    }
}
