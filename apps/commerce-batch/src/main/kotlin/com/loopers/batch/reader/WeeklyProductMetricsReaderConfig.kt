package com.loopers.batch.reader

import com.loopers.domain.analytics.PeriodType
import com.loopers.domain.analytics.ProductMetrics
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WeeklyProductMetricsReaderConfig(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    @StepScope
    fun weeklyProductMetricsReader( // Bean 이름 변경
        @Value("#{jobParameters['periodKey']}") periodKey: String,
        @Value("#{jobParameters['version']}") version: String
    ): JpaPagingItemReader<ProductMetrics> {
        val queryString = """
            SELECT pm FROM ProductMetrics pm
            WHERE pm.periodType = :periodType AND pm.periodKey = :periodKey AND pm.version = :version
            ORDER BY pm.orderCount DESC, pm.likes DESC, pm.views DESC
        """.trimIndent()

        return JpaPagingItemReaderBuilder<ProductMetrics>()
            .name("weeklyProductMetricsReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(100)
            .queryString(queryString)
            .parameterValues(
                mapOf(
                    "periodType" to PeriodType.WEEKLY,
                    "periodKey" to periodKey,
                    "version" to version
                )
            )
            .build()
    }
}
