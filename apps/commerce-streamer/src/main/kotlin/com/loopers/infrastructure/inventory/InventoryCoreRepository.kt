package com.loopers.infrastructure.inventory

import com.loopers.domain.inventory.Inventory
import com.loopers.domain.inventory.InventoryRepository
import org.springframework.stereotype.Component

@Component
class InventoryCoreRepository(
    private val inventoryJpaRepository: InventoryJpaRepository
) : InventoryRepository {
    override fun findAll(productIds: List<Long>, productOptionIds: List<Long>): List<Inventory> {
        return inventoryJpaRepository.findByProductIdInAndProductOptionIdIn(productIds, productOptionIds)
    }
}
