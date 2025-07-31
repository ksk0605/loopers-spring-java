package com.loopers.domain.payment;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentValidatorFactory validatorFactory;

    @Transactional
    public PaymentInfo process(PaymentCommand.Process command) {
        Payment payment = new Payment(command.orderId(), command.method(), PaymentStatus.PENDING, command.amount());
        payment.process(validatorFactory.getValidator(command.method()));
        return PaymentInfo.from(paymentRepository.save(payment));
    }
}
