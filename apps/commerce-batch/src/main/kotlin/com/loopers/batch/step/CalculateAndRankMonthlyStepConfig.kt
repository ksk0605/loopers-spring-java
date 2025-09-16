package com.loopers.batch.step

import com.loopers.domain.analytics.MvProductRankMonthly
import com.loopers.domain.analytics.ProductMetrics
import org.springframework.batch.core.Step
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class CalculateAndRankMonthlyStepConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    @Qualifier("monthlyProductMetricsReader") private val productMetricsReader: JpaPagingItemReader<ProductMetrics>,
    private val productRankProcessor: ItemProcessor<ProductMetrics, MvProductRankMonthly>,
    @Qualifier("monthlyProductRankWriter") private val productRankWriter: JpaItemWriter<MvProductRankMonthly>
) {

    @Bean
    fun calculateAndRankMonthlyStep(): Step {
        return StepBuilder("calculateAndRankMonthlyStep", jobRepository)
            .chunk<ProductMetrics, MvProductRankMonthly>(CHUNK_SIZE, transactionManager)
            .reader(productMetricsReader)
            .processor(productRankProcessor)
            .writer(productRankWriter)
            .build()
    }

    companion object {
        private const val CHUNK_SIZE = 100
    }
}
