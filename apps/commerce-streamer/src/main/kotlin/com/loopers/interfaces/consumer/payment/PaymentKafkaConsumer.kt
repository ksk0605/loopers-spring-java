package com.loopers.interfaces.consumer.payment

import com.loopers.application.product.ProductFacade
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.payment.PaymentEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class PaymentKafkaConsumer(
    private val productFacade: ProductFacade
) {
    @KafkaListener(
        topics = ["\${kafka.topic.payment.topic}"],
        groupId = "\${kafka.topic.payment.group-id}"
    )
    fun consume(message: InternalMessage<PaymentEvent>, acknowledgment: Acknowledgment) {
        productFacade.deductInventory(message.metadata.eventId, message.payload.orderId)
        acknowledgment.acknowledge()
    }
}
