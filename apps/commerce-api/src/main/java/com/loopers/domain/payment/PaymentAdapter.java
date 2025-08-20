package com.loopers.domain.payment;

public interface PaymentAdapter {
    PaymentRequestResult request(PaymentCommand.Approve command);
}
