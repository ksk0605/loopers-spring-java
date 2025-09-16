package com.loopers.batch.writer

import com.loopers.domain.analytics.MvProductRankMonthly
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MonthlyProductRankWriterConfig(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    fun monthlyProductRankWriter(): JpaItemWriter<MvProductRankMonthly> {
        return JpaItemWriterBuilder<MvProductRankMonthly>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }
}
