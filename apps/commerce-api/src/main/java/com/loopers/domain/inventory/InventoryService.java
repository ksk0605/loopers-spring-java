package com.loopers.domain.inventory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void deduct(InventoryCommand.Deduct command) {
        Inventories inventories = new Inventories(inventoryRepository.findAll(command));
        inventories.deductAll(command);
    }
}
