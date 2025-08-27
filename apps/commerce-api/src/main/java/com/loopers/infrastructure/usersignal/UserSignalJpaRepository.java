package com.loopers.infrastructure.usersignal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.usersignal.TargetType;
import com.loopers.domain.usersignal.UserSignal;

import jakarta.persistence.LockModeType;

public interface UserSignalJpaRepository extends JpaRepository<UserSignal, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserSignal u where u.targetType = :type and u.targetId = :targetId")
    Optional<UserSignal> findByTargetForUpdate(TargetType type, Long targetId);
}
