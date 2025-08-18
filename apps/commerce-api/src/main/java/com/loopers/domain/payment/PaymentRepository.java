package com.loopers.domain.payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);

    Optional<Payment> find(Long orderId);

    List<Payment> findAll(List<Long> orderIds);
}
