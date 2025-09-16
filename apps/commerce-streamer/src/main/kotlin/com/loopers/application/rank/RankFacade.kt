package com.loopers.application.rank

import com.loopers.domain.common.InternalMessage
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentEvent
import com.loopers.domain.rank.RankScoreCalculator
import com.loopers.domain.rank.RankService
import com.loopers.domain.usersignal.UserSignalEvent
import org.springframework.stereotype.Component

@Component
class RankFacade(
    private val rankService: RankService,
    private val orderService: OrderService,
    private val rankScoreCalculator: RankScoreCalculator,
) {
    fun applyUserSignalToRanking(messages: List<InternalMessage<UserSignalEvent>>) {
        val userSignalEvents = messages.map { it.payload }
        val scoreMap = rankScoreCalculator.calculateFromUserSignals(userSignalEvents)
        if (scoreMap.isNotEmpty()) {
            rankService.applyScores(scoreMap)
        }
    }

    fun applyOrderToRanking(messages: List<InternalMessage<PaymentEvent>>) {
        val orderIds = messages.map { it.payload.orderId }
        if (orderIds.isEmpty()) {
            return
        }
        val orders = orderService.findAll(orderIds)
        val scoreMap = rankScoreCalculator.calculateFromOrders(orders)
        if (scoreMap.isNotEmpty()) {
            rankService.applyScores(scoreMap)
        }
    }
}
