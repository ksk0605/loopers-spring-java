package com.loopers.application.like;

import com.loopers.domain.like.LikeTargetType;

public record LikeResult(
    Long userId,
    Long targetId,
    LikeTargetType targetType
) {
}
