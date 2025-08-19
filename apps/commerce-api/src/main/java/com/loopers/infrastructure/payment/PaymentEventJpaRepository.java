package com.loopers.infrastructure.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.payment.PaymentEvent;

public interface PaymentEventJpaRepository extends JpaRepository<PaymentEvent, Long> {
    Optional<PaymentEvent> findByOrderId(String orderId);
}
