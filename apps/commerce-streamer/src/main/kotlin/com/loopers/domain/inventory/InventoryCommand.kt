package com.loopers.domain.inventory

class InventoryCommand {
    data class Deduct(
        val options: List<Option>
    ) {
        fun productIds(): List<Long> {
            return options.map { it.productId }.toList()
        }

        fun productOptionIds(): List<Long> {
            return options.map { it.productOptionId }.toList()
        }
    }

    data class Option(
        val productId: Long,
        val productOptionId: Long,
        val quantity: Int
    )
}
