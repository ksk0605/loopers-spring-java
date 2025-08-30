package com.loopers.domain.like;

import java.util.Arrays;

public enum LikeTargetType {
    PRODUCT,
    BRAND;

    public static LikeTargetType from(String type) {
        return Arrays.stream(values())
            .filter(likeTargetType -> likeTargetType.name().equalsIgnoreCase(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 타입: " + type));
    }
}
