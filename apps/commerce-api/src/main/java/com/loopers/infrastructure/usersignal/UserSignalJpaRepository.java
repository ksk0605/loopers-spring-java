package com.loopers.infrastructure.usersignal;

import java.util.List;
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

    @Query("SELECT u FROM UserSignal u WHERE u.targetType = :targetType AND u.targetId IN (:productIds)")
    List<UserSignal> findAllByTargetIdIn(List<Long> productIds, TargetType targetType);
}
