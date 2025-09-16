package com.loopers.application.analytics

import com.loopers.domain.analytics.ProductMetricsCommand
import com.loopers.domain.analytics.ProductMetricsEvent
import com.loopers.domain.analytics.ProductMetricsService
import com.loopers.domain.common.EventDeduplicator
import com.loopers.domain.common.InternalMessage
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ProductMetricsFacade(
    private val eventDeduplicator: EventDeduplicator,
    private val productMetricsService: ProductMetricsService,
) {
    fun upsert(messages: List<InternalMessage<ProductMetricsEvent>>) {
        messages
            .filter { eventDeduplicator.checkAndMarkAsProcessed(it.metadata.eventId) }
            .forEach { message ->
                val evt = message.payload
                val ts = runCatching { LocalDateTime.parse(evt.generatedAt) }.getOrNull()
                val cmd = ProductMetricsCommand.Upsert(
                    productId = evt.productId,
                    periodType = evt.periodType,
                    periodKey = evt.periodKey,
                    version = evt.version,
                    likes = evt.likes,
                    views = evt.views,
                    orderCount = evt.orderCount,
                    generatedAt = ts,
                )
                productMetricsService.upsert(cmd)
            }
    }
}
