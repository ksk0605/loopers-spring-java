package com.loopers.domain.like;

public record LikeInfo(
    Long targetId,
    LikeTargetType targetType
) {
    public static LikeInfo from(Like like) {
        return new LikeInfo(like.getTarget().id(), like.getTarget().type());
    }
}
