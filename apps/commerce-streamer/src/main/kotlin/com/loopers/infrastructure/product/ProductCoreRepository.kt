package com.loopers.infrastructure.product

import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductRepository
import org.springframework.stereotype.Component

@Component
class ProductCoreRepository(
    private val productJpaRepository: ProductJpaRepository
) : ProductRepository {
    override fun findAll(productIds: List<Long>): List<Product> {
        return productJpaRepository.findAllByIdIn(productIds)
    }
}
