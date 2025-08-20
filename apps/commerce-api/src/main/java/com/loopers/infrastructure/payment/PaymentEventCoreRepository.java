package com.loopers.infrastructure.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentEventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventCoreRepository implements PaymentEventRepository {

    private final PaymentEventJpaRepository paymentEventJpaRepository;

    @Override
    public PaymentEvent save(PaymentEvent paymentEvent) {
        return paymentEventJpaRepository.save(paymentEvent);
    }

    @Override
    public Optional<PaymentEvent> findByOrderId(String orderId) {
        return paymentEventJpaRepository.findByOrderId(orderId);
    }

    @Override
    public List<PaymentEvent> findAllPendingPayments() {
        return paymentEventJpaRepository.findAllPendingPayments();
    }
}
