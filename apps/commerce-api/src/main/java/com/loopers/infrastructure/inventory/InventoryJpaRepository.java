package com.loopers.infrastructure.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.loopers.domain.inventory.Inventory;

import jakarta.persistence.LockModeType;

public interface InventoryJpaRepository extends JpaRepository<Inventory, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Inventory> findByProductIdAndProductOptionId(Long productId, Long productOptionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findByProductIdInAndProductOptionIdIn(List<Long> productIds, List<Long> productOptionIds);

    List<Inventory> findAllByProductOptionIdIn(List<Long> optionIds);
}
