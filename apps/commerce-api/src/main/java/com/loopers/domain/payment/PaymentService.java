package com.loopers.domain.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentValidatorFactory validatorFactory;

    public Payment process(Long orderId, PaymentMethod method, BigDecimal amount) {
        Payment payment = new Payment(orderId, method, PaymentStatus.PENDING, amount);
        payment.process(validatorFactory.getValidator(method));
        return paymentRepository.save(payment);
    }
}
