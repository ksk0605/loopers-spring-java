package com.loopers.infrastructure.common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

@SpringBootTest
class RedisEventDeduplicatorTest {

    @Autowired
    private lateinit var redisEventDeduplicator: RedisEventDeduplicator

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun `동일한 이벤트 ID에 대해 첫 호출은 true, 두 번째 호출은 false를 반환해야 한다`() {
        // arrange
        val eventId = "idempotency-test-event-12345"
        val expectedRedisKey = "processed:event:$eventId"

        // act1
        val firstAttempt = redisEventDeduplicator.checkAndMarkAsProcessed(eventId)

        // assert1
        assertThat(firstAttempt).isTrue()
        assertThat(redisTemplate.hasKey(expectedRedisKey)).isTrue()
        
        val ttl = redisTemplate.getExpire(expectedRedisKey)
        assertThat(ttl).isGreaterThan(0)
        assertThat(ttl).isLessThanOrEqualTo(Duration.ofDays(2).seconds)

        // act2
        val secondAttempt = redisEventDeduplicator.checkAndMarkAsProcessed(eventId)

        // assert2
        assertThat(secondAttempt).isFalse()
    }
}

