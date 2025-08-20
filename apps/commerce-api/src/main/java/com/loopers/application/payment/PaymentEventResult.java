package com.loopers.application.payment;

import java.time.LocalDateTime;

import com.loopers.domain.payment.PaymentEvent;

public record PaymentEventResult(
    Long id,
    String buyerId,
    String orderId,
    boolean isPaymentDone,
    String transactionKey,
    String method,
    String status,
    Long amount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime approvedAt
) {
    public static PaymentEventResult from(PaymentEvent event) {
        return new PaymentEventResult(
            event.getId(),
            event.getBuyerId(),
            event.getOrderId(),
            event.isPaymentDone(),
            event.getTransactionKey(),
            event.getMethod().name(),
            event.getStatus().name(),
            event.getAmount().longValue(),
            event.getCreatedAt(),
            event.getUpdatedAt(),
            event.getApprovedAt()
        );
    }
}
