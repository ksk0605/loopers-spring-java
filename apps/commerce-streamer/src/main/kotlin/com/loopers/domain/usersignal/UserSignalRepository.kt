package com.loopers.domain.usersignal

interface UserSignalRepository {
    fun findForUpdate(targetId: Long, targetType: TargetType): UserSignal?
}
