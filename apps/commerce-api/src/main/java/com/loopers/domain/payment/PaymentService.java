package com.loopers.domain.payment;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentValidatorFactory validatorFactory;

    public Payment create(PaymentCommand.Pay command) {
        Payment payment = new Payment(command.orderId(), command.method(), PaymentStatus.PENDING, command.amount());
        payment.process(validatorFactory.getValidator(command.method()));
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getByOrderId(Long orderId) {
        return paymentRepository.find(orderId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "주문에 대한 결제 정보를 찾을 수 없습니다."));
    }

    public List<Payment> getAll(List<Long> orderIds) {
        return paymentRepository.findAll(orderIds);
    }
}
