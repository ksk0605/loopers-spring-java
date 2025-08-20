package com.loopers.domain.payment;

import java.util.Arrays;

public enum CardType {
    SAMSUNG,
    KB,
    HYUNDAI,
    ;

    public static CardType from(String name) {
        return Arrays.stream(CardType.values())
            .filter(cardType -> cardType.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 카드 유형입니다.: " + name));
    }
}
