package com.loopers.domain.payment;

import com.loopers.application.payment.PaymentResult;

public interface PaymentExecutor {
    boolean support(PaymentMethod method);

    PaymentResult executePayment(PaymentCommand.Request command);
}
