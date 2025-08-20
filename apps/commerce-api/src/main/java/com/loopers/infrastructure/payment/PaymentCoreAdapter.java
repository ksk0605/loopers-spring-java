package com.loopers.infrastructure.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentCommand.Approve;
import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.infrastructure.payment.pgsimulator.PgSimulatorClient;
import com.loopers.infrastructure.payment.pgsimulator.exception.PgSimulatorException;
import com.loopers.infrastructure.payment.pgsimulator.request.PgSimulatorPaymentRequest;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorApiResponse;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorTransactionDetailResponse;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorTransactionResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCoreAdapter implements PaymentAdapter {
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
            PgSimulatorApiResponse<PgSimulatorTransactionResponse> response = pgSimulatorClient.request(request,
                    command.userId());

            PgSimulatorTransactionResponse data = response.data();
            return PaymentRequestResult.success(
                    data.getTransactionKey(),
                    PaymentStatus.from(data.getStatus()),
                    data.getReason());
        } catch (PgSimulatorException e) {
            log.error("PG 시뮬레이터 결제 실패. HttpStatus: {}, ErrorCode: {}, Message: {}", e.getHttpStatus(),
                    e.getErrorCode(), e.getMessage());
            int status = e.getHttpStatus();
            if (status >= 400 && status < 500) {
                return PaymentRequestResult.fail(e.getMessage());
            } else {
                String customMessage = "결제 시스템에 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                return PaymentRequestResult.fail(customMessage);
            }
        } catch (Exception e) {
            log.error("결제 시스템 연동 중 알 수 없는 오류가 발생했습니다.", e);
            String customMessage = "결제 시스템 연동 중 알 수 없는 오류가 발생했습니다.";
            return PaymentRequestResult.fail(customMessage);
        }
    }

    @Override
    public TransactionInfo getTransaction(String transactionKey, String userId) {
        PgSimulatorApiResponse<PgSimulatorTransactionDetailResponse> response = pgSimulatorClient.getTransaction(transactionKey, userId);
        PgSimulatorTransactionDetailResponse data = response.data();
        return new TransactionInfo(
            data.transactionKey(),
            data.orderId(),
            data.cardType().toCardType(),
            data.cardNo(),
            BigDecimal.valueOf(data.amount()),
            data.status().toPaymentStatus(),
            data.reason());
    }
}
