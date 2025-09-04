package com.loopers.domain.product

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class ProductService(
    private val productRepository: ProductRepository,
    private val productEventPublisher: ProductEventPublisher,
    private val productCacheRepository: ProductCacheRepository
) {
    fun deduct(command: ProductCommand.Deduct) {
        val products = productRepository.findAll(command.productIds())
        command.options
            .forEach { option ->
                val product = products.find { it.id == option.productId } ?: throw CoreException(ErrorType.NOT_FOUND)
                product.deduct(option.productOptionId, option.quantity)
            }
        val soldOutProducts = products.filter { it.isSoldOut() }
        soldOutProducts.forEach { it.markAsSoldOut() }

        if (soldOutProducts.isNotEmpty()) {
            val soldOutProductIds = soldOutProducts.mapNotNull { it.id }
            productEventPublisher.publishSoldOutEvent(soldOutProductIds)
        }
    }

    fun deleteCaches(event: ProductSoldOutEvent) {
        event.productIds.forEach {
            productCacheRepository.deleteProductInfo(it)
        }
    }
}
