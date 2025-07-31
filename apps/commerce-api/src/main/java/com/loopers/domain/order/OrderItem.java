package com.loopers.domain.order;

import static com.loopers.support.util.RequireUtils.requireNotNull;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
    private Long productId;
    private Long productOptionId;
    private Integer quantity;

    public OrderItem(Long productId, Long productOptionId, Integer quantity) {
        this.productId = requireNotNull(productId, "주문 아이템 생성은 상품 ID가 필수입니다.");
        this.productOptionId = requireNotNull(productOptionId, "주문 아이템 생성은 상품 옵션 ID가 필수입니다.");
        this.quantity = requireNotNull(quantity, "주문 아이템 생성은 수량이 필수입니다.");
    }
}
