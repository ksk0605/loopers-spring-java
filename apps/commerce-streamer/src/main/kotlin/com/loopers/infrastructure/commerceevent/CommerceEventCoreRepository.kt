package com.loopers.infrastructure.commerceevent

import com.loopers.domain.commerceevent.CommerceEvent
import com.loopers.domain.commerceevent.CommerceEventRepository
import org.springframework.stereotype.Component

@Component
class CommerceEventCoreRepository(
    private val commerceEventJpaRepository: CommerceEventJpaRepository
) : CommerceEventRepository {
    override fun find(eventId: String): CommerceEvent? {
        return commerceEventJpaRepository.findByEventId(eventId)
    }
}
