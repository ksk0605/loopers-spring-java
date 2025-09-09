package com.loopers.domain.usersignal

data class UserSignalEvent(
    val type: UserSignalEventType,
    val targetId: Long,
    val targetType: TargetType
)

enum class UserSignalEventType {
    LIKE, UNLIKE, VIEWED
}
