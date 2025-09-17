package com.loopers.domain.rank;

import java.util.Arrays;

public enum PeriodType {
    DAILY, WEEKLY, MONTHLY;

    public static PeriodType from(String periodType) {
        return Arrays.stream(values())
            .filter(type -> type.name().equalsIgnoreCase(periodType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 기간 타입입니다.: " + periodType));
    }
}
