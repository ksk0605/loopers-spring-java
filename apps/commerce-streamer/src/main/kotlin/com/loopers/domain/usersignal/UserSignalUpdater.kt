package com.loopers.domain.usersignal

interface UserSignalUpdater {
    fun supports(type: UserSignalEventType): Boolean

    fun update(event: UserSignalEvent)
}
