package com.loopers.application.usersignal

import com.loopers.domain.common.InternalMessage
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalUpdaterRegistry
import org.springframework.stereotype.Component

@Component
class UserSignalFacade(
    private val registry: UserSignalUpdaterRegistry
) {
    fun updateUserSignal(messages: List<InternalMessage<UserSignalEvent>>) {
        messages.forEach { message ->
            val userSignalEvent = message.payload
            registry.getUpdater(userSignalEvent.type).update(userSignalEvent)
        }
    }
}
