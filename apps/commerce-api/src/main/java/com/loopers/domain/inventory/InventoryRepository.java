package com.loopers.domain.inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    Optional<Inventory> find(Long productId, Long productOptionId);

    Inventory save(Inventory inventory);

    List<Inventory> findAll(InventoryCommand.Deduct command);

    List<Inventory> findAll(List<Long> optionIds);
}
