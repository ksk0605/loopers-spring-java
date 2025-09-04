package com.loopers.infrastructure.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.inventory.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryCoreRepository implements InventoryRepository {
    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    public Optional<Inventory> find(Long productId, Long productOptionId) {
        return inventoryJpaRepository.findByProductIdAndProductOptionId(productId, productOptionId);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryJpaRepository.save(inventory);
    }

    @Override
    public List<Inventory> findAll(List<Long> productIds, List<Long> productOptionIds) {
        return inventoryJpaRepository.findByProductIdInAndProductOptionIdIn(productIds, productOptionIds);
    }

    @Override
    public List<Inventory> findAll(List<Long> optionIds) {
        return inventoryJpaRepository.findAllByProductOptionIdIn(optionIds);
    }
}
