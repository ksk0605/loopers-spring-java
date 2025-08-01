package com.loopers.domain.like;

public interface LikeRepository {
    Like save(Like like);

    boolean exists(Long userId, LikeTarget target);

    void delete(Long userId, LikeTarget target);

    Long count(LikeTarget likeTarget);
}
