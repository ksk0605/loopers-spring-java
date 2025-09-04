package com.loopers.domain.inventory;

import java.util.List;

public class Inventories {
    private final List<Inventory> items;

    public Inventories(List<Inventory> items) {
        this.items = items;
    }

    public void reverse(InventoryCommand.Reverse command) {
        command.options()
            .forEach(option -> reverseItem(option.productId(), option.productOptionId(), option.quantity()));
    }

    private void reverseItem(Long productId, Long productOptionId, Integer quantity) {
        items.stream()
            .filter(inventory -> inventory.isOptionOf(productId, productOptionId))
            .findAny()
            .ifPresent(inventory -> inventory.reserve(quantity));
    }
}
