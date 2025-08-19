package com.loopers.domain.payment;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentValidatorFactory validatorFactory;

    public Payment create(PaymentCommand.Create command) {
        Payment payment = new Payment(command.orderId(), command.method(), PaymentStatus.PENDING, command.amount());
        payment.process(validatorFactory.getValidator(command.method()));
        return paymentRepository.save(payment);
    }
}
