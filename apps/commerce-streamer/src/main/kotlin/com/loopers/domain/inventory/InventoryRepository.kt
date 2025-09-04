package com.loopers.domain.inventory

interface InventoryRepository {
    fun findAll(productIds: List<Long>, productOptionIds: List<Long>): List<Inventory>
}
