package com.loopers.batch.step

import com.loopers.domain.analytics.MvProductRankWeeklyRepository
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
class CleanupWeeklyRankStepConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val cleanupWeeklyRankTableTasklet: Tasklet
) {

    @Bean
    fun cleanupWeeklyRankStep(): Step {
        return StepBuilder("cleanupWeeklyRankStep", jobRepository)
            .tasklet(cleanupWeeklyRankTableTasklet, transactionManager)
            .build()
    }
}

@StepScope
@Configuration
class CleanupWeeklyRankTableTasklet(
    private val repository: MvProductRankWeeklyRepository,
    @Value("#{jobParameters['periodKey']}") private val periodKey: String,
    @Value("#{jobParameters['version']}") private val version: String
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        repository.delete(periodKey, version)
        return RepeatStatus.FINISHED
    }
}
