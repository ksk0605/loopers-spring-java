package com.loopers.domain.usersignal;

import lombok.Getter;

@Getter
public class TargetLikeCount {
    private TargetType targetType;
    private Long targetId;
    private Long likeCount;

    public TargetLikeCount(TargetType targetType, Long targetId, Long likeCount) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.likeCount = likeCount;
    }

    public static TargetLikeCount from(UserSignal signal) {
        return new TargetLikeCount(
            signal.getTargetType(),
            signal.getTargetId(),
            signal.getLikeCount()
        );
    }
}
