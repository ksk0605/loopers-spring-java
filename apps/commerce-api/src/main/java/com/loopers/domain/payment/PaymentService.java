package com.loopers.domain.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentCommand.Callback;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentAdapter paymentAdapter;

    public PaymentEvent create(PaymentCommand.Create command) {
        PaymentEvent paymentEvent = new PaymentEvent(command.orderId(), command.amount());
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

    public PaymentRequestResult request(Approve command) {
        PaymentRequestResult result = paymentAdapter.request(command);
        if (!result.isSuccess()) {
            throw new CoreException(ErrorType.INTERNAL_ERROR, result.reason());
        }
        return result;
    }
}
