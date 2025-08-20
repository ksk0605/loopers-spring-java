package com.loopers.infrastructure.payment;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentExcutor;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.pgsimulator.exception.PgSimulatorException;
import com.loopers.infrastructure.payment.pgsimulator.PgSimulatorClient;
import com.loopers.infrastructure.payment.pgsimulator.request.PgSimulatorPaymentRequest;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorApiResponse;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorTransactionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
            "http://localhost:8080/api/v1/payments/callback");

        try {
            PgSimulatorApiResponse<PgSimulatorTransactionResponse> response = pgSimulatorClient.request(request, command.userId());

            PgSimulatorTransactionResponse data = response.data();
            return PaymentRequestResult.success(
                data.getTransactionKey(),
                PaymentStatus.from(data.getStatus()),
                data.getReason()
            );
        } catch (PgSimulatorException e) {
            log.error("PG 시뮬레이터 결제 실패. 코드: {}, 메시지: {}", e.getErrorCode(), e.getMessage());
            return PaymentRequestResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("결제 요청 중 알 수 없는 오류가 발생했습니다.", e);
            return PaymentRequestResult.fail("결제 요청 중 알 수 없는 오류가 발생했습니다.");
        }
    }
}
