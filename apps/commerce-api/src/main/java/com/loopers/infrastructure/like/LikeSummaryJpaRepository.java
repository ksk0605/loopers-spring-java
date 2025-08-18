package com.loopers.infrastructure.like;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.TargetLikeCount;

import jakarta.persistence.LockModeType;

public interface LikeSummaryJpaRepository extends JpaRepository<LikeSummary, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LikeSummary l where l.target = :target")
    Optional<LikeSummary> findByTargetForUpdate(LikeTarget target);

    List<TargetLikeCount> findAllByTargetIn(List<LikeTarget> targets);
}
