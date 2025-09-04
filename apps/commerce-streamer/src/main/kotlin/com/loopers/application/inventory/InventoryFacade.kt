package com.loopers.application.inventory

import com.loopers.domain.commerceevent.CommerceEventService
import com.loopers.domain.inventory.InventoryCommand
import com.loopers.domain.inventory.InventoryService
import com.loopers.domain.order.OrderService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InventoryFacade(
    private val commerceEventService: CommerceEventService,
    private val orderService: OrderService,
    private val inventoryService: InventoryService
) {
    @Transactional
    fun deduct(eventId: String, orderId: String) {
        if (commerceEventService.isAlreadyProcessed(eventId)) {
            return
        }

        val order = orderService.find(orderId)
        val options = order.items.map { InventoryCommand.Option(it.productId, it.productOptionId, it.quantity) }.toList()
        val command = InventoryCommand.Deduct(options)
        inventoryService.deduct(command)

        commerceEventService.markAsProcessed(eventId)
        orderService.complete(orderId);
    }
}
