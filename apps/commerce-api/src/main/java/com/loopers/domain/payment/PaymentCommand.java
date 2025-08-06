package com.loopers.domain.payment;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Process(
        Long orderId, PaymentMethod method, BigDecimal amount
    ) {
    }

    public record Pay(
        Long orderId, PaymentMethod method, BigDecimal amount
    ) {
    }
}
