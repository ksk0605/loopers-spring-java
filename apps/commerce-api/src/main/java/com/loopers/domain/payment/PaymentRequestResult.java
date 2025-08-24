package com.loopers.domain.payment;

public record PaymentRequestResult(
    String transactionKey,
    PaymentStatus status,
    String reason,
    boolean isSuccess
) {
    public static PaymentRequestResult success(String transactionKey, PaymentStatus status, String reason) {
        return new PaymentRequestResult(transactionKey, status, reason, true);
    }

    public static PaymentRequestResult fail(String reason) {
        return new PaymentRequestResult(null, null, reason, false);
    }
}
