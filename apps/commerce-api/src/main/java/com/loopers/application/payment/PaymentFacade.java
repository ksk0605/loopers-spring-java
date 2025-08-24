package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentCommand.Request;
import com.loopers.domain.payment.PaymentExecutorRegistry;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentExecutorRegistry registry;

    public PaymentResult requestPayment(Request command) {
        return registry.executePayment(command);
    }
}
