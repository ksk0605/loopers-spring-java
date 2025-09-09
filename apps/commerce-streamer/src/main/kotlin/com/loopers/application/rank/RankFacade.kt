package com.loopers.application.rank

import com.loopers.domain.common.InternalMessage
import com.loopers.domain.payment.PaymentEvent
import com.loopers.domain.usersignal.UserSignalEvent
import org.springframework.stereotype.Component

@Component
class RankFacade {
    fun applyUserSignalToRanking(messages: List<InternalMessage<UserSignalEvent>>) {
        // TODO
    }

    fun applyOrderToRanking(messages: List<InternalMessage<PaymentEvent>>) {
        // TODO
    }
}
