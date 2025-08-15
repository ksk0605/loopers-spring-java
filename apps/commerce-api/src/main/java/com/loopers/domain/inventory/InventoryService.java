package com.loopers.domain.inventory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public Map<Long, Integer> getStockQuantities(List<Long> optionIds) {
        return inventoryRepository.findAll(optionIds)
            .stream()
            .collect(Collectors.toMap(
                Inventory::getProductOptionId,
                Inventory::getQuantity
            ));
    }
}
