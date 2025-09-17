package com.loopers.batch.writer

import com.loopers.domain.analytics.MvProductRankWeekly
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WeeklyProductRankWriterConfig(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean
    fun weeklyProductRankWriter(): JpaItemWriter<MvProductRankWeekly> {
        return JpaItemWriterBuilder<MvProductRankWeekly>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }
}
