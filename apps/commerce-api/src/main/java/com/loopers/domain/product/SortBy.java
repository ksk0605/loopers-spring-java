package com.loopers.domain.product;

import java.util.Arrays;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public enum SortBy {
    LIKES_DESC, LATEST, PRICE_ASC;

    public static SortBy from(String sort) {
        return Arrays.stream(values())
            .filter(sortBy -> sortBy.name().equals(sort.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 정렬 조건입니다." + sort.toUpperCase()));
    }

    public boolean isLatest() {
        return this == LATEST;
    }

    public boolean isPriceAsc() {
        return this == PRICE_ASC;
    }
}
