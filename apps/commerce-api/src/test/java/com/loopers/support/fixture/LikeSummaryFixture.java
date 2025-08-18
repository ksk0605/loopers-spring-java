package com.loopers.support.fixture;

import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTargetType;

public class LikeSummaryFixture {
    private Long targetId = 1L;
    private LikeTargetType targetType = LikeTargetType.PRODUCT;

    public static LikeSummaryFixture aLikeSummary() {
        return new LikeSummaryFixture();
    }

    public LikeSummaryFixture targetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }

    public LikeSummaryFixture targetType(LikeTargetType targetType) {
        this.targetType = targetType;
        return this;
    }

    public LikeSummary build() {
        return new LikeSummary(targetId, targetType);
    }
}
