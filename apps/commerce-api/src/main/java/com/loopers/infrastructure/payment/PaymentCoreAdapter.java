package com.loopers.infrastructure.payment;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentCommand.Request;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.infrastructure.payment.pgsimulator.PgSimulatorClient;
import com.loopers.infrastructure.payment.pgsimulator.PgSimulatorDto;
import com.loopers.infrastructure.payment.pgsimulator.exception.PgSimulatorException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCoreAdapter implements PaymentAdapter {
    @Value("${payment.callback-url}")
    private String callbackUrl;
    private final PgSimulatorClient pgSimulatorClient;

    @Retry(name = "pg-simulator-payment")
    @CircuitBreaker(name = "pg-simulator-payment", fallbackMethod = "fallback")
    @Override
    public PaymentRequestResult request(Request command) {
        PgSimulatorDto.Request request = new PgSimulatorDto.Request(
            command.orderId(),
            command.cardType().name(),
            command.cardNo(),
            command.amount().longValue(),
            callbackUrl);

        var response = pgSimulatorClient.request(request,
            command.userId());
        var data = response.data();

        return PaymentRequestResult.success(
            data.transactionKey(),
            data.status().toPaymentStatus(),
            data.reason());
    }

    public PaymentRequestResult fallback(Request command, PgSimulatorException exception) {
        log.error("PG 시뮬레이터 결제 실패. HttpStatus: {}, ErrorCode: {}, Message: {}", exception.getHttpStatus(),
            exception.getErrorCode(), exception.getMessage());
        int status = exception.getHttpStatus();
        if (status >= 400 && status < 500) {
            return PaymentRequestResult.fail(exception.getMessage());
        } else {
            String customMessage = "결제 시스템에 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
            return PaymentRequestResult.fail(customMessage);
        }
    }

    public PaymentRequestResult fallback(Request command, Exception exception) {
        log.error("결제 시스템 연동 중 알 수 없는 오류가 발생했습니다.", exception);
        String customMessage = "결제 시스템 연동 중 알 수 없는 오류가 발생했습니다.";
        return PaymentRequestResult.fail(customMessage);
    }

    @Override
    public TransactionInfo getTransaction(String transactionKey, String userId) {
        var response = pgSimulatorClient.getTransaction(transactionKey,
            userId);
        var data = response.data();
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
