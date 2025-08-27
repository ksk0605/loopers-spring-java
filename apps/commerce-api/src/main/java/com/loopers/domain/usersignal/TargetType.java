package com.loopers.domain.usersignal;

import java.util.Arrays;

public enum TargetType {
    PRODUCT,
    BRAND;

    public static TargetType from(String type) {
        return Arrays.stream(values()).filter(t -> t.name().equals(type)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 타입: " + type));
    }
}
