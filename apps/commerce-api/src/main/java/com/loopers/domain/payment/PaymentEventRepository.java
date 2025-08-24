package com.loopers.domain.payment;

import java.util.List;
import java.util.Optional;

public interface PaymentEventRepository {
    PaymentEvent save(PaymentEvent paymentEvent);

    Optional<PaymentEvent> findByOrderId(String orderId);

    List<PaymentEvent> findAllPendingPayments();
}
