package com.loopers.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;

@Getter
public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private OrderStatus status;
    LocalDateTime orderDate;

    public Order(Long userId, List<OrderItem> items) {
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 생성은 유저 ID가 필수입니다.");
        }
        this.userId = userId;
        this.items = items;
        this.orderDate = LocalDateTime.now();
    }

    public void place() {
        if (this.items.isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목이 비어있습니다.");
        }
        this.status = OrderStatus.PENDING_PAYMENT;
    }
}
