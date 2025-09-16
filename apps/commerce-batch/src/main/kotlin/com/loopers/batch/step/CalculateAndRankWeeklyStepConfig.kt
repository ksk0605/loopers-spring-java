package com.loopers.batch.step

import com.loopers.batch.processor.WeeklyProductRankProcessor
import com.loopers.domain.analytics.MvProductRankWeekly
import com.loopers.domain.analytics.ProductMetrics
import org.springframework.batch.core.Step
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class CalculateAndRankWeeklyStepConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    @Qualifier("weeklyProductMetricsReader") private val reader: JpaPagingItemReader<ProductMetrics>,
    private val processor: WeeklyProductRankProcessor,
    @Qualifier("weeklyProductRankWriter") private val writer: JpaItemWriter<MvProductRankWeekly>
) {

    @Bean
    fun calculateAndRankWeeklyStep(): Step {
        return StepBuilder("calculateAndRankWeeklyStep", jobRepository)
            .chunk<ProductMetrics, MvProductRankWeekly>(100, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }
}
