package com.loopers.infrastructure.like;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTarget;

public interface LikeSummaryJpaRepository extends JpaRepository<LikeSummary, Long> {
    Optional<LikeSummary> findByTarget(LikeTarget target);
}
