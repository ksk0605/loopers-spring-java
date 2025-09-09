package com.loopers.application.product

import com.loopers.domain.commerceevent.CommerceEventService
import com.loopers.domain.order.OrderService
import com.loopers.domain.product.ProductCommand
import com.loopers.domain.product.ProductService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductFacade(
    private val commerceEventService: CommerceEventService,
    private val orderService: OrderService,
    private val productService: ProductService
) {
    @Transactional
    fun deductInventory(eventId: String, orderId: String) {
        if (commerceEventService.isAlreadyProcessed(eventId)) {
            return
        }

        val order = orderService.find(orderId)
        val options = order.items.map { ProductCommand.Option(it.productId, it.productOptionId, it.quantity) }.toList()
        val command = ProductCommand.Deduct(options)
        productService.deduct(command)

        commerceEventService.markAsProcessed(eventId)
        orderService.complete(orderId);
    }
}
