package com.loopers.interfaces.consumer.analytics

import com.loopers.application.analytics.ProductMetricsFacade
import com.loopers.config.kafka.KafkaConfig
import com.loopers.domain.analytics.ProductMetricsEvent
import com.loopers.domain.common.InternalMessage
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class ProductMetricsKafkaConsumer(
    private val productMetricsFacade: ProductMetricsFacade
) {
    @KafkaListener(
        topics = ["\${kafka.topic.product-metrics.topic}"],
        containerFactory = KafkaConfig.BATCH_LISTENER,
        groupId = "\${kafka.topic.product-metrics.group-id}"
    )
    fun consume(records: List<InternalMessage<ProductMetricsEvent>>, acknowledgment: Acknowledgment) {
        productMetricsFacade.upsert(records)
        acknowledgment.acknowledge()
    }
}

