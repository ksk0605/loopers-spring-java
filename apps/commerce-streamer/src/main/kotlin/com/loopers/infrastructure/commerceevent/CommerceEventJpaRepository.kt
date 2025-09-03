package com.loopers.infrastructure.commerceevent

import com.loopers.domain.commerceevent.CommerceEvent
import org.springframework.data.jpa.repository.JpaRepository

interface CommerceEventJpaRepository : JpaRepository<CommerceEvent, Long> {
    fun findByEventId(eventId: String): CommerceEvent?
}
