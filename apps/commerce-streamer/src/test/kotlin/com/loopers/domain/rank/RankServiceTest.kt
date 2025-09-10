package com.loopers.domain.rank

import com.loopers.utils.RedisCleanUp
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootTest
class RankServiceTest {

    @Autowired
    private lateinit var redisCleanUp: RedisCleanUp

    @Autowired
    private lateinit var rankService: RankService

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @AfterEach
    fun tearDown() {
        redisCleanUp.truncateAll()
    }

    @Test
    fun `applyScores 호출 시 Redis ZSET에 점수가 정확히 누적된다`() {
        // arrange
        val scoreMap = mapOf(100L to 1.5, 200L to 0.5)

        // act
        rankService.applyScores(scoreMap)

        // assert
        val key = "ranking:all:" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val score = redisTemplate.opsForZSet().score(key, "100")

        assertThat(score).isEqualTo(1.5)
    }
}
