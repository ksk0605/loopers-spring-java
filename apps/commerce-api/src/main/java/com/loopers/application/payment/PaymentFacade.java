package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentCommand.Request;
import com.loopers.domain.payment.PaymentCommand.Sync;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentService;
import com.loopers.support.annotation.UseCase;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final PaymentAdapter paymentAdapter;

    public PaymentResult requestPayment(Request command) {
        paymentService.execute(command);
        PaymentRequestResult result = paymentAdapter.request(command);
        if (!result.isSuccess()) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, result.reason());
        }
        return new PaymentResult(result.transactionKey());
    }

    public void syncPayment(Sync command) {
        paymentService.sync(command);
    }
}
