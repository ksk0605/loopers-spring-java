package com.loopers.domain.payment;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentEventRepository paymentEventRepository;

    public PaymentEvent create(PaymentCommand.Create command) {
        PaymentEvent paymentEvent = new PaymentEvent(command.orderId(), command.amount());
        paymentEventRepository.save(paymentEvent);
        return paymentEvent;
    }
}
