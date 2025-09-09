package com.loopers.infrastructure.rank

import com.loopers.domain.rank.RankRepository
import com.loopers.infrastructure.redis.RedisCacheRepository
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class RankCoreRepository(
    private val redisCacheRepository: RedisCacheRepository
) : RankRepository {
    companion object {
        private const val KEY_PREFIX = "rank:all:"
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
    }

    override fun applyScores(scoreMap: Map<Long, Double>) {
        if (scoreMap.isEmpty()) return

        val key = generateKey(LocalDate.now())

        scoreMap.forEach { (productId, score) ->
            redisCacheRepository.incrementScore(key, productId.toString(), score)
        }

        redisCacheRepository.expire(key, Duration.ofDays(2))
    }

    private fun generateKey(date: LocalDate): String {
        return KEY_PREFIX + date.format(DATE_FORMATTER)
    }
}
