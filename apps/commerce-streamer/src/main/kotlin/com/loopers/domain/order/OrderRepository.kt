package com.loopers.domain.order

interface OrderRepository {
    fun find(orderId: String): Order?
}
