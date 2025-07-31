package com.loopers.domain.inventory;

import static com.loopers.support.util.RequireUtils.requireNotNull;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

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
        validateQuantity(quantity);

        this.productId = requireNotNull(productId);
        this.productOptionId = requireNotNull(productOptionId);
        this.quantity = requireNotNull(quantity);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new CoreException(ErrorType.BAD_REQUEST, "재고는 0 이상이어야 합니다.");
        }
    }

    public boolean canOrder(Integer orderQuantity) {
        return this.quantity >= orderQuantity;
    }
}
