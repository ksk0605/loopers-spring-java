package com.loopers.infrastructure.analytics

import com.loopers.domain.analytics.ProductMetricsEvent
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.common.Metadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.UUID

@Component
class ProductMetricsEventPublisher(
    private val kafkaTemplate: KafkaTemplate<Any, Any>,
    @Value("\${kafka.topic.product-metrics.topic}") private val topic: String,
    @Value("\${kafka.topic.product-metrics.version}") private val version: String,
) {
    fun publish(events: List<ProductMetricsEvent>) {
        if (events.isEmpty()) return
        val publishedAt = OffsetDateTime.now().toString()
        events.forEach { evt ->
            val message = InternalMessage(
                metadata = Metadata(eventId = UUID.randomUUID().toString(), version = version, publishedAt = publishedAt),
                payload = evt
            )
            kafkaTemplate.send(topic, message)
        }
    }
}

