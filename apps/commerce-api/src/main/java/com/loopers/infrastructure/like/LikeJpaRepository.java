package com.loopers.infrastructure.like;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.LikeTargetType;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndTarget(Long userId, LikeTarget target);

    void deleteByUserIdAndTarget(Long userId, LikeTarget target);

    Long countByTarget(LikeTarget likeTarget);

    List<Like> findAllByUserIdAndTargetType(Long userId, LikeTargetType targetType);
}
