package com.loopers.domain.payment;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Create(
        String orderId,
        BigDecimal amount
    ) {
    }

    public record Approve(
        String userId,
        String orderId,
        CardType cardType,
        String cardNo,
        BigDecimal amount,
        PaymentMethod method
    ) {
    }
}
