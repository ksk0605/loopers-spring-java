package com.loopers.domain.payment;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Create(
        String orderId,
        BigDecimal amount
    ) {
    }
}
