package com.loopers.domain.payment;

public interface PaymentTransactionRepository {
    PaymentTransaction save(PaymentTransaction transaction);
}
