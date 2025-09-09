package com.loopers.domain.rank

import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalEventType
import org.springframework.stereotype.Component

@Component
class RankScoreCalculator {

    fun calculateFromUserSignals(signals: List<UserSignalEvent>): Map<Long, Double> {
        return signals
            .filter { it.targetType == TargetType.PRODUCT }
            .map { signal ->
                val score = when (signal.type) {
                    UserSignalEventType.VIEWED -> RankWeight.VIEW.score
                    UserSignalEventType.LIKE -> RankWeight.LIKE.score
                    UserSignalEventType.UNLIKE -> -RankWeight.LIKE.score
                }
                signal.targetId to score
            }
            .groupBy { it.first }
            .mapValues { entry -> entry.value.sumOf { it.second } }
    }
}
