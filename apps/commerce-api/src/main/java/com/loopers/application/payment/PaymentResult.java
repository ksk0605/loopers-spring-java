package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentStatus;

public record PaymentResult(
    String transactionKey,
    PaymentStatus status
) {
}
