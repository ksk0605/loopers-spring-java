package com.loopers.batch.processor

import com.loopers.domain.analytics.PeriodType
import com.loopers.domain.analytics.ProductMetrics
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class MonthlyProductRankProcessorTest {

    private lateinit var monthlyProductRankProcessor: MonthlyProductRankProcessor

    @BeforeEach
    fun setUp() {
        monthlyProductRankProcessor = MonthlyProductRankProcessor(
            periodKey = "2025-09",
            version = "v1.0"
        )
    }

    @DisplayName("ProductMetrics를 MvProductRankMonthly로 변환하며, 점수와 순위를 정확히 부여한다")
    @Test
    fun `process_converts_and_assigns_score_and_rank_correctly`() {
        // arrange
        val productMetrics1 = createTestProductMetrics(
            productId = 101L,
            orderCount = 10, // score: 10*10 + 20*3 + 50*1 = 210
            likes = 20,
            views = 50
        )
        val productMetrics2 = createTestProductMetrics(
            productId = 102L,
            orderCount = 5,  // score: 5*10 + 15*3 + 30*1 = 125
            likes = 15,
            views = 30
        )

        // act
        val result1 = monthlyProductRankProcessor.process(productMetrics1)
        val result2 = monthlyProductRankProcessor.process(productMetrics2)

        // assert
        assertEquals(1, result1.rank) // 첫 번째 아이템은 rank가 1이어야 함
        assertEquals(210.0, result1.score)
        assertEquals(101L, result1.productId)

        assertEquals(2, result2.rank) // 두 번째 아이템은 rank가 2여야 함
        assertEquals(125.0, result2.score)
        assertEquals(102L, result2.productId)
    }

    private fun createTestProductMetrics(
        productId: Long,
        orderCount: Long,
        likes: Long,
        views: Long
    ): ProductMetrics {
        return ProductMetrics(
            productId,
            PeriodType.MONTHLY,
            "2025-09",
            "v1.0",
            likes,
            views,
            orderCount,
            LocalDateTime.now()
        )
    }
}
