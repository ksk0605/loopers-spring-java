package com.loopers.infrastructure.like;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTarget;

import jakarta.persistence.LockModeType;

public interface LikeSummaryJpaRepository extends JpaRepository<LikeSummary, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LikeSummary l where l.target = :target")
    Optional<LikeSummary> findByTargetForUpdate(LikeTarget target);
}
