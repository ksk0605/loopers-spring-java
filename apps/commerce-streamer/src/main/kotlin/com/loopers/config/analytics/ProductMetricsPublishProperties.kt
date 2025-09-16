package com.loopers.config.analytics

import com.loopers.domain.analytics.PeriodType
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "analytics.product-metrics")
data class ProductMetricsPublishProperties(
    val periodType: PeriodType = PeriodType.DAILY,
    val zoneId: String = "Asia/Seoul",
    val version: String,
)
