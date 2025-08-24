package com.loopers.infrastructure.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.payment.PaymentTransaction;

public interface PaymentTransactionJpaRepository extends JpaRepository<PaymentTransaction, Long> {
    
}
