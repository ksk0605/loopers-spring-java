package com.loopers.infrastructure.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.inventory.InventoryCommand;
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
    public List<Inventory> findAll(InventoryCommand.Deduct command) {
        List<Long> productIds = command.options().stream().map(InventoryCommand.Option::productId).toList();
        List<Long> productOptionIds = command.options().stream().map(InventoryCommand.Option::productOptionId).toList();

        List<Inventory> inventories = inventoryJpaRepository.findByProductIdInAndProductOptionIdIn(productIds, productOptionIds);

        if (inventories.size() != command.options().size()) {
            throw new IllegalArgumentException("Some inventories were not found for the provided product and option IDs.");
        }

        return inventories;
    }
}
