package com.loopers.infrastructure.inventory

import com.loopers.domain.inventory.Inventory
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryJpaRepository : JpaRepository<Inventory, Long> {
    fun findByProductIdInAndProductOptionIdIn(productIds: List<Long>, productOptionIds: List<Long>): List<Inventory>
}
