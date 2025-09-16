package com.loopers.interfaces.consumer.rank

import com.loopers.application.rank.RankFacade
import com.loopers.config.kafka.KafkaConfig
import com.loopers.domain.analytics.ProductMetricsPublishService
import com.loopers.domain.common.EventDeduplicator
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.payment.PaymentEvent
import com.loopers.domain.usersignal.UserSignalEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class RankKafkaConsumer(
    private val rankFacade: RankFacade,
    private val productMetricsPublishService: ProductMetricsPublishService,
    private val eventDeduplicator: EventDeduplicator,
) {
    @KafkaListener(
        topics = ["\${kafka.topic.user-signals.topic}"],
        containerFactory = KafkaConfig.BATCH_LISTENER,
        groupId = "\${kafka.topic.rank-user-signals.group-id}"
    )
    fun consumeUserSignalEvent(records: List<InternalMessage<UserSignalEvent>>, acknowledgment: Acknowledgment) {
        val filtered = records.filter { eventDeduplicator.checkAndMarkAsProcessed(it.metadata.eventId) }
        rankFacade.applyUserSignalToRanking(filtered)
        productMetricsPublishService.publishFromUserSignals(filtered)
        acknowledgment.acknowledge()
    }

    @KafkaListener(
        topics = ["\${kafka.topic.payment.topic}"],
        containerFactory = KafkaConfig.BATCH_LISTENER,
        groupId = "\${kafka.topic.rank-order.group-id}"
    )
    fun consumePaymentEvent(records: List<InternalMessage<PaymentEvent>>, acknowledgment: Acknowledgment) {
        val filtered = records.filter { eventDeduplicator.checkAndMarkAsProcessed(it.metadata.eventId) }
        rankFacade.applyOrderToRanking(filtered)
        productMetricsPublishService.publishFromOrders(filtered)
        acknowledgment.acknowledge()
    }
}
