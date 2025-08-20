package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentCommand.Callback;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;

    public PaymentResult approve(Approve command) {
        paymentService.execute(command);
        PaymentRequestResult result = paymentService.request(command);
        return new PaymentResult(result.transactionKey(), result.status());
    }

    public void handleCallback(Callback command) {
        paymentService.handleCallback(command);
    }
}
