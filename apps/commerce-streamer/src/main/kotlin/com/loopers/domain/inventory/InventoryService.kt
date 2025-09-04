package com.loopers.domain.inventory

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InventoryService(
    private val inventoryRepository: InventoryRepository
) {
    @Transactional
    fun deduct(command: InventoryCommand.Deduct) {
        val inventories = inventoryRepository.findAll(command.productIds(), command.productOptionIds())

        command.options.forEach { option ->
            val inventory = inventories.find {
                it.productId == option.productId && it.productOptionId == option.productOptionId
            } ?: throw CoreException(ErrorType.NOT_FOUND)
            inventory.deduct(option.quantity)
        }
    }
}
