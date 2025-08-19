package com.loopers.infrastructure.payment.pgsimulator.request;

public record PgSimulatorPaymentRequest(
    String orderId,
    String cardType,
    String cardNo,
    Long amount,
    String callbackUrl
) {
}
