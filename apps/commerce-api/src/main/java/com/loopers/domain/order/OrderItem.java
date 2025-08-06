package com.loopers.domain.order;

import static com.loopers.support.util.RequireUtils.requireNotNull;

import java.math.BigDecimal;

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
    private BigDecimal basePrice;
    private BigDecimal optionPrice;

    public OrderItem(Long productId, Long productOptionId, Integer quantity, BigDecimal basePrice, BigDecimal optionPrice) {
        this.productId = requireNotNull(productId, "주문 아이템 생성은 상품 ID가 필수입니다.");
        this.productOptionId = requireNotNull(productOptionId, "주문 아이템 생성은 상품 옵션 ID가 필수입니다.");
        this.quantity = requireNotNull(quantity, "주문 아이템 생성은 수량이 필수입니다.");
        this.basePrice = requireNotNull(basePrice);
        this.optionPrice = requireNotNull(optionPrice);
    }
}
