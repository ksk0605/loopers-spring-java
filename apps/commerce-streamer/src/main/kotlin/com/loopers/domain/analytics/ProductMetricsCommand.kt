package com.loopers.domain.analytics

import java.time.LocalDateTime

class ProductMetricsCommand {
    data class Upsert(
        val productId: Long,
        val periodType: PeriodType,
        val periodKey: String,
        val version: String,
        val likes: Long? = null,
        val views: Long? = null,
        val orderCount: Long? = null,
        val generatedAt: LocalDateTime? = null,
    )
}
