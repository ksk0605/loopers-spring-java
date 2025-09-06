package com.loopers.domain.usersignal

class UserSignalCommand {
    data class IncreaseLike(
        val targetId: Long,
        val targetType: TargetType
    )

    data class DecreaseLike(
        val targetId: Long,
        val targetType: TargetType
    )

    data class IncreaseView(
        val targetId: Long,
        val targetType: TargetType
    )
}
