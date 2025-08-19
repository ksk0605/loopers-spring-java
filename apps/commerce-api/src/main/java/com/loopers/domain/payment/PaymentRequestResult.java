package com.loopers.domain.payment;

import lombok.Getter;

@Getter
public class PaymentRequestResult {
    private final String transactionKey;
    private final PaymentStatus status;
    private final String reason;
    private boolean isSuccess;

    public PaymentRequestResult(String transactionKey, PaymentStatus status, String reason, boolean isSuccess) {
        this.transactionKey = transactionKey;
        this.status = status;
        this.reason = reason;
        this.isSuccess = isSuccess;
    }
}
