package com.loopers.application.usersignal

import com.loopers.domain.commerceevent.CommerceEventService
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalUpdaterRegistry
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserSignalFacade(
    private val registry: UserSignalUpdaterRegistry,
    private val commerceEventService: CommerceEventService
) {
    @Transactional
    fun updateUserSignal(messages: List<InternalMessage<UserSignalEvent>>) {
        messages
            .filter { message -> !commerceEventService.isAlreadyProcessed(message.metadata.eventId) }
            .forEach { message ->
                val userSignalEvent = message.payload
                registry.getUpdater(userSignalEvent.type).update(userSignalEvent)
                commerceEventService.markAsProcessed(message.metadata.eventId)
            }
    }
}
