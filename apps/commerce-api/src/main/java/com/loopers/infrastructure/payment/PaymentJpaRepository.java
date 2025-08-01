package com.loopers.infrastructure.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.payment.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);
}
