package com.loopers.application.analytics

import com.loopers.domain.analytics.PeriodType
import com.loopers.domain.analytics.ProductMetricsEvent
import com.loopers.domain.analytics.ProductMetricsRepository
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.common.Metadata
import com.loopers.support.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class ProductMetricsIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var facade: ProductMetricsFacade

    @Autowired
    private lateinit var repository: ProductMetricsRepository

    @Test
    fun `상품 메트릭 이벤트를 전달받아 실제 DB에 업서트로 저장한다`() {
        // arrange
        val msg1 = InternalMessage(
            metadata = Metadata(eventId = "e1", version = "1.0.0", publishedAt = LocalDateTime.now().toString()),
            payload = ProductMetricsEvent(
                productId = 10L,
                periodType = PeriodType.DAILY,
                periodKey = "2025-09-16",
                version = "v1",
                likes = 3,
                views = 20,
                orderCount = null,
                generatedAt = "2025-09-16T10:00:00"
            ),
        )
        val msg2 = InternalMessage(
            metadata = Metadata(eventId = "e2", version = "1.0.0", publishedAt = LocalDateTime.now().toString()),
            payload = ProductMetricsEvent(
                productId = 20L,
                periodType = PeriodType.DAILY,
                periodKey = "2025-09-16",
                version = "v1",
                likes = null,
                views = null,
                orderCount = 5,
                generatedAt = null,
            ),
        )

        // act
        facade.upsert(listOf(msg1, msg2))

        // assert
        val saved1 = repository.find(10L, PeriodType.DAILY, "2025-09-16", "v1").orElseThrow()
        assertThat(saved1.likes).isEqualTo(3)
        assertThat(saved1.views).isEqualTo(20)
        assertThat(saved1.orderCount).isEqualTo(0)

        val saved2 = repository.find(20L, PeriodType.DAILY, "2025-09-16", "v1").orElseThrow()
        assertThat(saved2.likes).isEqualTo(0)
        assertThat(saved2.views).isEqualTo(0)
        assertThat(saved2.orderCount).isEqualTo(5)
    }
}

