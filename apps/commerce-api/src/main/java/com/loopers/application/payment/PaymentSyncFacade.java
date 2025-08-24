package com.loopers.application.payment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;

import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentSyncFacade {
    private final PaymentService paymentService;
    private final PaymentAdapter paymentAdapter;

    public void syncPayment(PaymentCommand.Sync command) {
        paymentService.sync(command);
    }

    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
    public void syncPayments() {
        List<PaymentEvent> events = paymentService.getPendingPayments();
        for (var event : events) {
            TransactionInfo info = paymentAdapter.getTransaction(event.getTransactionKey(), event.getBuyerId());
            paymentService.sync(PaymentCommand.Sync.from(info));
        }
    }
}
