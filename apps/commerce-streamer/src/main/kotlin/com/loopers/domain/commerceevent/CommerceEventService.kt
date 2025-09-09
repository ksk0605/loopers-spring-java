package com.loopers.domain.commerceevent

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class CommerceEventService(
    private val commerceEventRepository: CommerceEventRepository
) {
    fun isAlreadyProcessed(eventId: String): Boolean {
        val event = commerceEventRepository.find(eventId)
            ?: throw CoreException(ErrorType.NOT_FOUND)
        return event.processed()
    }

    fun markAsProcessed(eventId: String) {
        val event = (commerceEventRepository.find(eventId)
            ?: throw CoreException(ErrorType.NOT_FOUND))
        event.markAsProcessed();
    }
}
