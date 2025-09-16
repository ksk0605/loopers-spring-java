package com.loopers.infrastructure.analytics

import com.loopers.domain.analytics.PeriodType
import com.loopers.domain.analytics.ProductMetricsEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.kafka.core.KafkaTemplate

class ProductMetricsEventPublisherTest {

    @Test
    fun `상품 메트릭 이벤틀르 발송한다`() {
        // arrange
        val kafkaTemplate = mock(KafkaTemplate::class.java) as KafkaTemplate<Any, Any>
        val publisher = ProductMetricsEventPublisher(kafkaTemplate, "product-metrics", "1.0.0")

        val events = listOf(
            ProductMetricsEvent(
                1L,
                PeriodType.DAILY,
                "2025-09-16",
                "v1",
                likes = 10,
                views = 100,
                orderCount = null,
                generatedAt = null
            ),
            ProductMetricsEvent(
                2L,
                PeriodType.DAILY,
                "2025-09-16",
                "v1",
                likes = 5,
                views = 40,
                orderCount = 3,
                generatedAt = null
            )
        )

        // act
        publisher.publish(events)

        // assert
        val captor = ArgumentCaptor.forClass(Any::class.java)
        verify(kafkaTemplate, times(2)).send(eq("product-metrics"), captor.capture())
        val messages = captor.allValues
        assertEquals(2, messages.size)
        val first = messages[0]
        assertNotNull(first)
    }
}
