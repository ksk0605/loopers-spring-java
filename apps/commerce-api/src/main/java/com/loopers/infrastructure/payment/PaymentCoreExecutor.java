package com.loopers.infrastructure.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentExcutor;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.pgsimulator.PgSimulatorClient;
import com.loopers.infrastructure.payment.pgsimulator.request.PgSimulatorPaymentRequest;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorApiResponse;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorTransactionResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentCoreExecutor implements PaymentExcutor {
    private final PgSimulatorClient pgSimulatorClient;

    @Override
    public PaymentRequestResult request(Approve command) {
        PgSimulatorPaymentRequest request = new PgSimulatorPaymentRequest(
            command.orderId(),
            command.cardType().name(),
            command.cardNo(),
            command.amount().longValue(),
            "http://localhost:8080");
        PgSimulatorApiResponse<PgSimulatorTransactionResponse> response = pgSimulatorClient.request(request, command.userId());

        return new PaymentRequestResult(
            response.data().getTransactionKey(),
            PaymentStatus.from(response.data().getStatus()),
            response.data().getReason(),
            true);
    }
}
