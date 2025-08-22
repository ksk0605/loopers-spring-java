package com.loopers.domain.payment;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointPaymentExecutor implements PaymentExecutor {
    private final UserService userService;

    @Override
    public boolean support(PaymentMethod method) {
        return method == PaymentMethod.POINT;
    }

    @Override
    public PaymentResult executePayment(PaymentCommand.Request command) {
        userService.usePoint(command.userId(), command.amount());
        String transactionKey = String.format("POINT-%s-%s", command.userId(), UUID.randomUUID());
        return new PaymentResult(transactionKey);
    }
}
