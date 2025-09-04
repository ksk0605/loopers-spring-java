package com.loopers.interfaces.consumer.payment

import com.loopers.config.kafka.KafkaConfig
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.payment.PaymentEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class PaymentKafkaConsumer {
    @KafkaListener(
        topics = ["\${kafka.topic.payment.topic}"],
        containerFactory = KafkaConfig.BATCH_LISTENER,
        groupId = "\${kafka.topic.payment.group-id}"
    )
    fun consume(messages: List<InternalMessage<PaymentEvent>>, acknowledgment: Acknowledgment) {
        println(messages)
        acknowledgment.acknowledge()
    }
}
