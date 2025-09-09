package com.loopers.domain.order

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderService(
    private val orderRepository: OrderRepository
) {
    fun find(orderId: String): Order {
        return orderRepository.find(orderId)
            ?: throw CoreException(ErrorType.NOT_FOUND)
    }

    fun complete(orderId: String) {
        val order = orderRepository.find(orderId)
            ?: throw CoreException(ErrorType.NOT_FOUND)
        order.complete()
    }

    @Transactional(readOnly = true)
    fun findAll(orderIds: List<String>): List<Order> {
        return orderRepository.findByOrderIdIn(orderIds)
    }
}
