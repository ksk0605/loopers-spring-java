package com.loopers.batch.step

import com.loopers.domain.analytics.MvProductRankMonthlyRepository
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class CleanupMonthlyRankStepConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val cleanupRankTableTasklet: Tasklet
) {

    @Bean
    fun cleanupMonthlyRankStep(): Step {
        return StepBuilder("cleanupMonthlyRankStep", jobRepository)
            .tasklet(cleanupRankTableTasklet, transactionManager)
            .build()
    }
}

@StepScope
@Configuration
class CleanupRankTableTasklet(
    private val repository: MvProductRankMonthlyRepository,
    @Value("#{jobParameters['periodKey']}") private val periodKey: String,
    @Value("#{jobParameters['version']}") private val version: String
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        repository.delete(periodKey, version)
        return RepeatStatus.FINISHED
    }
}
