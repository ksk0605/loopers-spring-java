package com.loopers.domain.product

interface ProductRepository {
    fun findAll(productIds: List<Long>): List<Product>
}
