package com.loopers.domain.payment;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Create(
        Long orderId, PaymentMethod method, BigDecimal amount
    ) {
    }
}
