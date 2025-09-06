package com.loopers.infrastructure.order

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderRepository
import org.springframework.stereotype.Component

@Component
class OrderCoreRepository(
    private val orderJpaRepository: OrderJpaRepository
) : OrderRepository {
    override fun find(orderId: String): Order? {
        return orderJpaRepository.findByOrderId(orderId)
    }
}
