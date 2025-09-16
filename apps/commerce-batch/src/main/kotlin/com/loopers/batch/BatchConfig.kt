package com.loopers.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class BatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
) {

    @Bean
    fun sampleStep(): Step = StepBuilder("sampleStep", jobRepository)
        .tasklet { _, _ ->
            println("[commerce-batch] Sample tasklet executed")
            RepeatStatus.FINISHED
        }
        .transactionManager(transactionManager)
        .build()

    @Bean
    fun sampleJob(): Job = JobBuilder("sampleJob", jobRepository)
        .start(sampleStep())
        .build()
}
