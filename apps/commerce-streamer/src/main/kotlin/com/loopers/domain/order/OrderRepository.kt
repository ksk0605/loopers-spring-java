package com.loopers.domain.order

interface OrderRepository {
    fun find(orderId: String): Order?

    fun findByOrderIdIn(orderIds: List<String>): List<Order>
}
