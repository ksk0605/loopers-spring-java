package com.loopers.domain.payment;

public interface PaymentExcutor {
    PaymentRequestResult request(PaymentCommand.Approve command);
}
