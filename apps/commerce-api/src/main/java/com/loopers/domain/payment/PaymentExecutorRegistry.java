package com.loopers.domain.payment;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.application.payment.PaymentResult;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentExecutorRegistry {
    private final List<PaymentExecutor> adaptors;

    public PaymentResult executePayment(PaymentCommand.Request command) {
        return adaptors.stream()
            .filter(adaptor -> adaptor.support(command.method()))
            .findFirst()
            .map(adaptor -> adaptor.executePayment(command))
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "지원하지 않는 결제 방식입니다."));
    }
}
