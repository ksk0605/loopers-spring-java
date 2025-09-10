package com.loopers.infrastructure.common

import com.loopers.domain.common.EventDeduplicator
import com.loopers.infrastructure.redis.RedisCacheRepository
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisEventDeduplicator(
    private val redisCacheRepository: RedisCacheRepository
) : EventDeduplicator {

    companion object {
        private const val KEY_PREFIX = "processed:event:"
        private val EXPIRE_DURATION = Duration.ofDays(2)
    }

    override fun checkAndMarkAsProcessed(eventId: String): Boolean {
        val key = KEY_PREFIX + eventId
        return redisCacheRepository.setIfAbsent(key, "processed", EXPIRE_DURATION)
    }
}
