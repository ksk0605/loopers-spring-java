package com.loopers.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentInfo(
    Long orderId,
    PaymentMethod method,
    PaymentStatus status,
    BigDecimal amount,
    LocalDateTime paymentDate
) {
    public static PaymentInfo from(Payment payment) {
        return new PaymentInfo(
            payment.getOrderId(),
            payment.getMethod(),
            payment.getStatus(),
            payment.getAmount(),
            payment.getPaymentDate()
        );
    }
}
