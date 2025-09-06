package com.loopers.infrastructure.usersignal

import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignal
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface UserSignalJpaRepository : JpaRepository<UserSignal, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u from UserSignal u WHERE u.targetId = :targetId AND u.targetType = :targetType")
    fun findForUpdate(targetId: Long, targetType: TargetType): UserSignal?
}
