package com.loopers.domain.payment;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.payment.PaymentCommand.Request;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Transactional
    public PaymentEvent create(PaymentCommand.Create command) {
        PaymentEvent paymentEvent = new PaymentEvent(command.orderId(), command.amount(), command.username());
        paymentEventRepository.save(paymentEvent);
        return paymentEvent;
    }

    @Transactional
    public void execute(Request command) {
        PaymentEvent event = paymentEventRepository.findByOrderId(command.orderId())
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "결제 이벤트를 찾을 수 없습니다."));
        if (!event.isValid(command.amount())) {
            throw new CoreException(ErrorType.CONFLICT, "주문 결제 금액이 올바르지 않습니다.");
        }
        event.execute();
        paymentEventRepository.save(event);
    }

    @Transactional
    public void sync(PaymentCommand.Sync command) {
        PaymentEvent event = paymentEventRepository.findByOrderId(command.orderId())
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "결제 이벤트를 찾을 수 없습니다."));
        event.sync(command);
        PaymentTransaction transaction = PaymentTransaction.of(command, event.getId());
        paymentTransactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<PaymentEvent> getPendingPayments() {
        return paymentEventRepository.findAllPendingPayments();
    }
}
