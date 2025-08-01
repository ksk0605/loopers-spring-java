package com.loopers.domain.inventory;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryValidationService {
    private final InventoryRepository inventoryRepository;

    public void validate(Long productId, Long productOptionId, Integer quantity) {
        Inventory inventory = inventoryRepository.find(productId, productOptionId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "재고를 찾을 수 없습니다. 상품 ID: " + productId));

        if (!inventory.canOrder(quantity)) {
            throw new CoreException(ErrorType.CONFLICT,
                "재고가 부족합니다. 요청: " + quantity + ", 재고: " + inventory.getQuantity());
        }
    }
}
