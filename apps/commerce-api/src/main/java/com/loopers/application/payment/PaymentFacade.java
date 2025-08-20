package com.loopers.application.payment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;

import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentCommand.Request;
import com.loopers.domain.payment.PaymentCommand.Sync;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.support.annotation.UseCase;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final PaymentAdapter paymentAdapter;

    public PaymentResult requestPayment(Request command) {
        paymentService.execute(command);
        PaymentRequestResult result = paymentAdapter.request(command);
        if (!result.isSuccess()) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, result.reason());
        }
        return new PaymentResult(result.transactionKey());
    }

    public void syncPayment(Sync command) {
        paymentService.sync(command);
    }

    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
    public void syncPayments() {
        List<PaymentEvent> events = paymentService.getPendingPayments();
        for (var event : events) {
            TransactionInfo info = paymentAdapter.getTransaction(event.getTransactionKey(), event.getBuyerId());
            paymentService.sync(Sync.from(info));
        }
    }
}
