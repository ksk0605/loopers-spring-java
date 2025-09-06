package com.loopers.infrastructure.usersignal

import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignal
import com.loopers.domain.usersignal.UserSignalRepository
import org.springframework.stereotype.Component

@Component
class UserSignalCoreRepository(
    val userSignalJpaRepository: UserSignalJpaRepository
) : UserSignalRepository {

    override fun findForUpdate(targetId: Long, targetType: TargetType): UserSignal? {
        return userSignalJpaRepository.findForUpdate(targetId, targetType)
    }
}
