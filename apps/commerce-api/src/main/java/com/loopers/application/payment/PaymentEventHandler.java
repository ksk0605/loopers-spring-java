package com.loopers.application.payment;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.order.OrderCreatedEvent;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventHandler {
    private final PaymentService paymentService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        paymentService.create(new PaymentCommand.Create(
            event.getOrderId(),
            event.getTotalPrice(),
            event.getUserId(),
            event.getUserName()));
    }
}
