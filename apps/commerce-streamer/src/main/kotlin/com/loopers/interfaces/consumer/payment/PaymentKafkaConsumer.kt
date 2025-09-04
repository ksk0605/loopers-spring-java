package com.loopers.interfaces.consumer.payment

import com.loopers.application.inventory.InventoryFacade
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.payment.PaymentEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class PaymentKafkaConsumer(
    private val inventoryFacade: InventoryFacade
) {
    @KafkaListener(
        topics = ["\${kafka.topic.payment.topic}"],
        groupId = "\${kafka.topic.payment.group-id}"
    )
    fun consume(message: InternalMessage<PaymentEvent>, acknowledgment: Acknowledgment) {
        inventoryFacade.deduct(message.metadata.eventId, message.payload.orderId)
        acknowledgment.acknowledge()
    }
}
