package com.loopers.domain.rank

import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalEventType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RankScoreCalculatorTest {
    private val calculator = RankScoreCalculator()

    @Test
    fun `유저 시그널 이벤트 목록으로 랭킹 점수를 계산한다`() {
        // arrange
        val userSignalEvents = listOf(
            // 100번 상품: 좋아요 1회, 좋아요 취소 1회, 조회 1회 -> 0.2 - 0.2 + 0.1 = 0.1
            UserSignalEvent(UserSignalEventType.VIEWED, 100L, TargetType.PRODUCT),
            UserSignalEvent(UserSignalEventType.UNLIKE, 100L, TargetType.PRODUCT),
            UserSignalEvent(UserSignalEventType.LIKE, 100L, TargetType.PRODUCT),
            // 101번 상품: 좋아요 2회 -> 0.2 + 0.2 = 0.4
            UserSignalEvent(UserSignalEventType.LIKE, 101L, TargetType.PRODUCT),
            UserSignalEvent(UserSignalEventType.LIKE, 101L, TargetType.PRODUCT),
        )

        // act
        var scoreMap = calculator.calculateFromUserSignals(userSignalEvents)

        // assert
        assertThat(scoreMap).hasSize(2)
        assertThat(scoreMap[100L]).isEqualTo(0.1)
        assertThat(scoreMap[101L]).isEqualTo(0.4)
    }
}
