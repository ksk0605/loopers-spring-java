package com.loopers.domain.usersignal

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DecreaseLikeCounter(
    private val userSignalRepository: UserSignalRepository
) : UserSignalUpdater {
    override fun supports(type: UserSignalEventType): Boolean {
        return type == UserSignalEventType.UNLIKE
    }

    override fun update(event: UserSignalEvent) {
        val signal = userSignalRepository.findForUpdate(event.targetId, event.targetType)
            ?: throw CoreException(ErrorType.NOT_FOUND)
        signal.decreaseLikeCount()
    }
}
