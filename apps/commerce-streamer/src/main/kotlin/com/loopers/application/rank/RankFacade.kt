package com.loopers.application.rank

import com.loopers.domain.common.EventDeduplicator
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.payment.PaymentEvent
import com.loopers.domain.rank.RankScoreCalculator
import com.loopers.domain.rank.RankService
import com.loopers.domain.usersignal.UserSignalEvent
import org.springframework.stereotype.Component

@Component
class RankFacade(
    private val eventDeduplicator: EventDeduplicator,
    private val rankService: RankService,
    private val rankScoreCalculator: RankScoreCalculator
) {
    fun applyUserSignalToRanking(messages: List<InternalMessage<UserSignalEvent>>) {
        val userSignalEvents = messages
            .filter { eventDeduplicator.checkAndMarkAsProcessed(it.metadata.eventId) }
            .map { it.payload }
        val scoreMap = rankScoreCalculator.calculateFromUserSignals(userSignalEvents)
        if (scoreMap.isNotEmpty()) {
            rankService.applyScores(scoreMap)
        }
    }

    fun applyOrderToRanking(messages: List<InternalMessage<PaymentEvent>>) {
        // TODO: 주문 점수 계산
    }
}
