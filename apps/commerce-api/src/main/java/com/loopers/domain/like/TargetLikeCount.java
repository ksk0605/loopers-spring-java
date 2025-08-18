package com.loopers.domain.like;

import lombok.Getter;

@Getter
public class TargetLikeCount {
    private LikeTarget target;
    private Long likeCount;

    public TargetLikeCount(LikeTarget target, Long likeCount) {
        this.target = target;
        this.likeCount = likeCount;
    }
}
