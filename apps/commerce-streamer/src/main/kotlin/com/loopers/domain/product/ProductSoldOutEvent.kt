package com.loopers.domain.product

data class ProductSoldOutEvent(
    val productIds: List<Long>
)
