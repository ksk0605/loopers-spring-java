package com.loopers.domain.rank

import com.loopers.domain.order.Order
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

    fun calculateFromOrders(orders: List<Order>): Map<Long, Double> {
        return orders
            .flatMap { order ->
                order.items.map { item ->
                    val score = 1.0 * RankWeight.ORDER.score
                    item.productId to score
                }
            }
            .groupBy { it.first }
            .mapValues { entry -> entry.value.sumOf { it.second } }
    }
}
