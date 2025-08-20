package com.loopers.domain.payment;

import java.math.BigDecimal;

public record TransactionInfo(
    String transactionKey,
    String orderId,
    CardType cardType,
    String cardNo,
    BigDecimal amount,
    PaymentStatus status,
    String reason
) {
    
}
