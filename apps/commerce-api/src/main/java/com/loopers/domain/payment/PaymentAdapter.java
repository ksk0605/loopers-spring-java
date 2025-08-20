package com.loopers.domain.payment;

public interface PaymentAdapter {
    PaymentRequestResult request(PaymentCommand.Approve command);

    TransactionInfo getTransaction(String transactionKey, String userId);
}
