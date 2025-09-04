package com.loopers.domain.product

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class ProductService(
    private val productRepository: ProductRepository
) {
    fun deduct(command: ProductCommand.Deduct) {
        val products = productRepository.findAll(command.productIds())
        command.options
            .forEach { option ->
                val product = products.find { it.id == option.productId } ?: throw CoreException(ErrorType.NOT_FOUND)
                product.deduct(option.productOptionId, option.quantity)
            }
    }
}
