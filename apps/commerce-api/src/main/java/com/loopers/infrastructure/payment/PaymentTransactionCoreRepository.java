package com.loopers.infrastructure.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentTransaction;
import com.loopers.domain.payment.PaymentTransactionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentTransactionCoreRepository implements PaymentTransactionRepository{
    private final PaymentTransactionJpaRepository paymentTransactionJpaRepository;


    @Override
    public PaymentTransaction save(PaymentTransaction transaction) {
        return paymentTransactionJpaRepository.save(transaction);
    }    
}
