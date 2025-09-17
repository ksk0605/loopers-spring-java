package com.loopers.batch.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MonthlyProductRankJobConfig(
    private val jobRepository: JobRepository,
    @Qualifier("cleanupMonthlyRankStep") private val cleanupRankStep: Step,
    @Qualifier("calculateAndRankMonthlyStep") private val calculateAndRankStep: Step
) {

    @Bean
    fun calculateMonthlyProductRankJob(): Job {
        return JobBuilder("calculateMonthlyProductRankJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(cleanupRankStep)
            .next(calculateAndRankStep)
            .build()
    }
}
