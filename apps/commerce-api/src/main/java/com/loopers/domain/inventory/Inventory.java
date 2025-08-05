package com.loopers.domain.inventory;

import static com.loopers.support.util.RequireUtils.require;
import static com.loopers.support.util.RequireUtils.requireNotNull;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory extends BaseEntity {
    private static final int MIN_QUANTITY = 0;

    private Long productId;
    private Long productOptionId;
    private Integer quantity;

    public Inventory(Long productId, Long productOptionId, Integer quantity) {
        this.productId = requireNotNull(productId);
        this.productOptionId = requireNotNull(productOptionId);
        this.quantity = requireNotNull(quantity);
        require(quantity >= MIN_QUANTITY, "재고는 0 이상이어야 합니다.");
    }

    public boolean canOrder(Integer orderQuantity) {
        return this.quantity >= orderQuantity;
    }

    public boolean isOptionOf(Long productId, Long productOptionId) {
        return this.productId.equals(productId) && this.productOptionId.equals(productOptionId);
    }

    public void deduct(Integer quantity) {
        this.quantity -= quantity;
    }
}
