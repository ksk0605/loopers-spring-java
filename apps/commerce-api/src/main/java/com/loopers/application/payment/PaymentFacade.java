package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentCommand.Callback;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.support.annotation.UseCase;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final PaymentAdapter paymentAdapter;

    public PaymentResult requestPayment(Approve command) {
        paymentService.execute(command);
        PaymentRequestResult result = paymentAdapter.request(command);
        if (!result.isSuccess()) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, result.reason());
        }
        return new PaymentResult(result.transactionKey());
    }

    public void handleCallback(Callback command) {
        PaymentEvent event = paymentService.getEvent(command.orderId());
        TransactionInfo info = paymentAdapter.getTransaction(command.transactionKey(), event.getBuyerId());
        paymentService.update(info);
    }
}
