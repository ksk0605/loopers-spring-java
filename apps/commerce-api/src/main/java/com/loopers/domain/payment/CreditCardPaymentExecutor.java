package com.loopers.domain.payment;

import org.springframework.stereotype.Component;

import com.loopers.application.payment.PaymentResult;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreditCardPaymentExecutor implements PaymentExecutor {
    private final PaymentAdapter paymentAdapter;
    private final PaymentService paymentService;

    @Override
    public boolean support(PaymentMethod method) {
        return method == PaymentMethod.CREDIT_CARD;
    }

    @Override
    public PaymentResult executePayment(PaymentCommand.Request command) {
        paymentService.execute(command);
        PaymentRequestResult result = paymentAdapter.request(command);
        if (!result.isSuccess()) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, result.reason());
        }
        return new PaymentResult(result.transactionKey());
    }
}
