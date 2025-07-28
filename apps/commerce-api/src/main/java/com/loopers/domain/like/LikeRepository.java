package com.loopers.domain.like;

public interface LikeRepository {
    Like save(Like like);

    boolean existsByTarget(LikeTarget target);

    void deleteByTarget(LikeTarget target);
}
