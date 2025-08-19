package com.loopers.domain.payment;

import java.util.Arrays;

public enum PaymentStatus {
    NOT_STARTED,
    EXECUTING,
    SUCCESS,
    FAILED,
    UNKNOWN,;

    public static PaymentStatus from(String status) {
        return Arrays.stream(values())
            .filter(s -> s.name().equalsIgnoreCase(status))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
