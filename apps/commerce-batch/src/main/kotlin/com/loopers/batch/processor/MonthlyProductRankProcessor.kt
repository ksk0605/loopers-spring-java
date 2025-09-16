package com.loopers.batch.processor

import com.loopers.domain.analytics.MvProductRankMonthly
import com.loopers.domain.analytics.ProductMetrics
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
@StepScope
class MonthlyProductRankProcessor(
    @Value("#{jobParameters['periodKey']}") private val periodKey: String,
    @Value("#{jobParameters['version']}") private val version: String
) : ItemProcessor<ProductMetrics, MvProductRankMonthly> {

    private val rankCounter = AtomicInteger(1)

    override fun process(item: ProductMetrics): MvProductRankMonthly {
        val currentRank = rankCounter.getAndIncrement()

        val score = with(item) {
            (orderCount * 10.0) + (likes * 3.0) + (views * 1.0)
        }

        return MvProductRankMonthly(
            periodKey,
            version,
            currentRank,
            item.productId,
            score,
            item.likes,
            item.views,
            item.orderCount
        )
    }
}
