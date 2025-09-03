package com.loopers.domain.usersignal

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class UserSignalUpdaterRegistry(
    val updaters: List<UserSignalUpdater>
) {
    fun getUpdater(type: UserSignalEventType): UserSignalUpdater {
        return updaters.find { it.supports(type) }
            ?: throw CoreException(ErrorType.NOT_FOUND, "지원하지 않는 이벤트 타입입니다.")
    }
}
