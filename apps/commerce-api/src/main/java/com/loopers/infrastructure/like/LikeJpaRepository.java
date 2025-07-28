package com.loopers.infrastructure.like;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTarget;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    boolean existsByTarget(LikeTarget target);

    void deleteByTarget(LikeTarget target);

    Long countByTarget(LikeTarget likeTarget);
}
