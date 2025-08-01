package com.loopers.application.like;

import com.loopers.domain.like.LikeTargetType;

public class LikeCriteria {

    public record LikeProduct(String userId, Long productId, LikeTargetType targetType) {
    }
}
