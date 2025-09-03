package com.loopers.domain.commerceevent

interface CommerceEventRepository {
    fun find(eventId: String): CommerceEvent?
}
