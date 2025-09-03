package com.loopers.application

import com.loopers.domain.common.InternalEvent
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalUpdaterRegistry
import org.springframework.stereotype.Component

@Component
class UserSignalFacade(
    private val registry: UserSignalUpdaterRegistry
) {
    fun updateUserSignal(events: List<InternalEvent<UserSignalEvent>>) {
        events.forEach { internalEvent ->
            val userSignalEvent = internalEvent.payload
            registry.getUpdater(userSignalEvent.type).update(userSignalEvent)
        }
    }
}
