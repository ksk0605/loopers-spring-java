package com.loopers.domain.analytics

import com.loopers.domain.analytics.PeriodType as PeriodTypeEnum

data class ProductMetricsEvent(
    val productId: Long,
    val periodType: PeriodTypeEnum,
    val periodKey: String,
    val version: String,
    val likes: Long? = null,
    val views: Long? = null,
    val orderCount: Long? = null,
    val generatedAt: String? = null,
)
