package com.loopers.domain.payment;

import java.util.Arrays;

public enum PaymentMethod {
    CREDIT_CARD,
    POINT;

    public static PaymentMethod from(String name) {
        return Arrays.stream(PaymentMethod.values())
            .filter(method -> method.name().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 결제 방법입니다.: " + name));
    }
}
