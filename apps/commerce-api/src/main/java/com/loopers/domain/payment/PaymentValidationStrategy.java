package com.loopers.domain.payment;

public interface PaymentValidationStrategy {
    void validate(Payment payment);

    boolean supports(PaymentMethod method);
}
