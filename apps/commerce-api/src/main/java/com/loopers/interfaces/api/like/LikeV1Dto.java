package com.loopers.interfaces.api.like;

import com.loopers.application.like.LikeResult;

public class LikeV1Dto {
    public record LikeResponse(Long userId, Long targetId, String targetType) {
        public static LikeResponse from(LikeResult result) {
            return new LikeResponse(
                result.userId(),
                result.targetId(),
                result.targetType().name()
            );
        }
    }
}
