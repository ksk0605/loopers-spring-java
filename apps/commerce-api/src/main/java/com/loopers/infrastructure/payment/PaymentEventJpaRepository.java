package com.loopers.infrastructure.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.loopers.domain.payment.PaymentEvent;

public interface PaymentEventJpaRepository extends JpaRepository<PaymentEvent, Long> {
    Optional<PaymentEvent> findByOrderId(String orderId);

    @Query("SELECT p FROM PaymentEvent p WHERE p.status IN ('NOT_STARTED', 'EXECUTING')")
    List<PaymentEvent> findAllPendingPayments();
}
