package com.loopers.batch.processor

import com.loopers.domain.analytics.MvProductRankWeekly
import com.loopers.domain.analytics.ProductMetrics
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
@StepScope
class WeeklyProductRankProcessor( // 클래스 이름 변경
    @Value("#{jobParameters['periodKey']}") private val periodKey: String,
    @Value("#{jobParameters['version']}") private val version: String
) : ItemProcessor<ProductMetrics, MvProductRankWeekly> { // 반환 타입 변경

    private val rankCounter = AtomicInteger(1)

    override fun process(item: ProductMetrics): MvProductRankWeekly { // 반환 타입 변경
        val currentRank = rankCounter.getAndIncrement()

        val score = with(item) {
            (orderCount * 10.0) + (likes * 3.0) + (views * 1.0)
        }

        // MvProductRankWeekly 객체를 생성하여 반환
        return MvProductRankWeekly(
            this.periodKey,
            this.version,
            currentRank,
            item.productId,
            score,
            item.likes,
            item.views,
            item.orderCount
        )
    }
}
