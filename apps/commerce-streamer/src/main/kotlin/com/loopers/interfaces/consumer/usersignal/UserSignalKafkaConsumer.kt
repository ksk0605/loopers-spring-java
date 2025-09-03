package com.loopers.interfaces.consumer.usersignal

import com.loopers.application.usersignal.UserSignalFacade
import com.loopers.config.kafka.KafkaConfig
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.usersignal.UserSignalEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class UserSignalKafkaConsumer(
    private val userSignalFacade: UserSignalFacade
) {
    @KafkaListener(
        topics = ["\${kafka.topic.user-signals.topic}"],
        containerFactory = KafkaConfig.BATCH_LISTENER,
    )
    fun consume(messages: List<InternalMessage<UserSignalEvent>>, acknowledgment: Acknowledgment) {
        userSignalFacade.updateUserSignal(messages)
        acknowledgment.acknowledge()
    }
}
