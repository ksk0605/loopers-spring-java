package com.loopers.support.fixture;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTargetType;

public class LikeFixture {
    private Long userId = 1L;
    private Long targetId = 1L;
    private LikeTargetType targetType = LikeTargetType.PRODUCT;

    public static LikeFixture aLike() {
        return new LikeFixture();
    }

    public LikeFixture userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LikeFixture targetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }

    public LikeFixture targetType(LikeTargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public Like build() {
        return new Like(
            userId,
            targetId,
            targetType
        );
    }

    private LikeFixture() {
    }
}
