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
class MonthlyProductMetricsReaderConfig(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    @StepScope
    fun monthlyProductMetricsReader(
        @Value("#{jobParameters['periodKey']}") periodKey: String,
        @Value("#{jobParameters['version']}") version: String
    ): JpaPagingItemReader<ProductMetrics> {
        val queryString = """
            SELECT pm FROM ProductMetrics pm
            WHERE pm.periodType = :periodType AND pm.periodKey = :periodKey AND pm.version = :version
            ORDER BY pm.orderCount DESC, pm.likes DESC, pm.views DESC
        """.trimIndent()

        return JpaPagingItemReaderBuilder<ProductMetrics>()
            .name("monthlyProductMetricsReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString(queryString)
            .parameterValues(
                mapOf(
                    "periodType" to PeriodType.MONTHLY,
                    "periodKey" to periodKey,
                    "version" to version
                )
            )
            .build()
    }

    companion object {
        private const val CHUNK_SIZE = 100
    }
}
