package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.annotation.UseCase;
import com.loopers.domain.payment.PaymentExcutor;
import com.loopers.domain.payment.PaymentRequestResult;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final PaymentExcutor paymentExcutor;

    public PaymentResult approve(Approve command) {
        paymentService.execute(command);
        PaymentRequestResult result = paymentExcutor.request(command);
        return new PaymentResult(result.transactionKey(), result.status());
    }
}
