package com.loopers.domain.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentEvent create(PaymentCommand.Create command) {
        PaymentEvent paymentEvent = new PaymentEvent(command.orderId(), command.amount(), command.username());
        paymentEventRepository.save(paymentEvent);
        return paymentEvent;
    }

    public void execute(Approve command) {
        PaymentEvent event = paymentEventRepository.findByOrderId(command.orderId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "결제 이벤트를 찾을 수 없습니다."));
        if (!event.isValid(command.amount())) {
            throw new CoreException(ErrorType.CONFLICT, "주문 결제 금액이 올바르지 않습니다.");
        }
        event.execute();
        paymentEventRepository.save(event);
    }

    @Transactional
    public void update(TransactionInfo info) {
        PaymentEvent event = paymentEventRepository.findByOrderId(info.orderId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "결제 이벤트를 찾을 수 없습니다."));

        PaymentTransaction transaction = new PaymentTransaction(
                info.orderId(),
                info.transactionKey(),
                info.cardType(),
                info.amount(),
                info.status(),
                event.getId());
        paymentTransactionRepository.save(transaction);
    }

    public PaymentEvent getEvent(String orderId) {
        return paymentEventRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "결제 이벤트를 찾을 수 없습니다."));
    }
}
