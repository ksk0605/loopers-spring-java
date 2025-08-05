package com.loopers.domain.inventory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void deduct(InventoryCommand.Deduct command) {
        List<Inventory> inventories = inventoryRepository.findAll(command);
        inventories.forEach(
            inventory -> command.options()
                .stream()
                .filter(option -> inventory.isOptionOf(option.productId(), option.productOptionId()))
                .findFirst()
                .ifPresent(option -> {
                    if (inventory.canOrder(option.quantity())) {
                        inventory.deduct(option.quantity());
                        inventoryRepository.save(inventory);
                    }
                })
        );
    }
}
