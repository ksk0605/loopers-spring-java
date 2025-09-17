package com.loopers.batch.processor

import com.loopers.domain.analytics.PeriodType
import com.loopers.domain.analytics.ProductMetrics
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class WeeklyProductRankProcessorTest {

    private lateinit var weeklyProcessor: WeeklyProductRankProcessor

    @BeforeEach
    fun setUp() {
        weeklyProcessor = WeeklyProductRankProcessor(
            periodKey = "2025-W38",
            version = "v1.0"
        )
    }

    @Test
    @DisplayName("주간 Processor가 점수와 순위를 정확히 부여하고 MvProductRankWeekly로 변환한다")
    fun `process_converts_to_weekly_rank_correctly`() {
        // given
        val metrics = createTestProductMetrics(101L, 10, 20, 50)

        // when
        val result = weeklyProcessor.process(metrics)!!

        // then
        assertEquals(1, result.rank)
        assertEquals(210.0, result.score)
        assertEquals(101L, result.productId)
        assertEquals("2025-W38", result.periodKey)
    }

    private fun createTestProductMetrics(
        productId: Long,
        orderCount: Long,
        likes: Long,
        views: Long
    ): ProductMetrics {
        return ProductMetrics(
            productId,
            PeriodType.WEEKLY,
            "2025-W38",
            "v1.0",
            likes,
            views,
            orderCount,
            LocalDateTime.now()
        )
    }
}
