package com.loopers.domain.analytics

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductMetricsService(
    private val productMetricsRepository: ProductMetricsRepository,
) {
    @Transactional
    fun upsert(command: ProductMetricsCommand.Upsert) {
        productMetricsRepository.upsert(
            command.productId,
            command.periodType,
            command.periodKey,
            command.version,
            command.likes,
            command.views,
            command.orderCount,
            command.generatedAt,
        )
    }
}

