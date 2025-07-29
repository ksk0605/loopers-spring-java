package com.loopers.domain.inventory;

import java.util.Optional;

public interface InventoryRepository {
    Optional<Inventory> find(Long productId, Long productOptionId);

    Inventory save(Inventory inventory);
}
