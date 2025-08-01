package com.loopers.infrastructure.payment;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> find(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId);
    }
}
