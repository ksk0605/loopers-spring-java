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
class WeeklyProductRankJobConfig(
    private val jobRepository: JobRepository,
    @Qualifier("cleanupWeeklyRankStep") private val cleanupWeeklyRankStep: Step,
    @Qualifier("calculateAndRankWeeklyStep") private val calculateAndRankWeeklyStep: Step
) {

    @Bean
    fun calculateWeeklyProductRankJob(): Job {
        return JobBuilder("calculateWeeklyProductRankJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(cleanupWeeklyRankStep)
            .next(calculateAndRankWeeklyStep)
            .build()
    }
}
