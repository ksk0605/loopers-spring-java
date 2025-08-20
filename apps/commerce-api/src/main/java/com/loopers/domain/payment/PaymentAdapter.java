package com.loopers.domain.payment;

public interface PaymentAdapter {
    PaymentRequestResult request(PaymentCommand.Request command);

    TransactionInfo getTransaction(String transactionKey, String userId);
}
