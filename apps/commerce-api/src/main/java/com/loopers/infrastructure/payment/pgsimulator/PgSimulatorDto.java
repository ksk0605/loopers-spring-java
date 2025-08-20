package com.loopers.infrastructure.payment.pgsimulator;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentStatus;

public class PgSimulatorDto {
    public record Request(
        String orderId,
        String cardType,
        String cardNo,
        Long amount,
        String callbackUrl
    ) {
    }

    public record TransactionResponse(
        String transactionKey,
        TransactionStatusResponse status,
        String reason
    ) {
    }

    public record TransactionDetailResponse(
        String transactionKey,
        String orderId,
        CardTypeDto cardType,
        String cardNo,
        Long amount,
        TransactionStatusResponse status,
        String reason) {
    }

    public enum CardTypeDto {
        SAMSUNG,
        KB,
        HYUNDAI,
        ;

        public CardType toCardType() {
            return switch (this) {
                case SAMSUNG -> CardType.SAMSUNG;
                case KB -> CardType.KB;
                case HYUNDAI -> CardType.HYUNDAI;
            };
        }
    }

    public enum TransactionStatusResponse {
        PENDING,
        SUCCESS,
        FAILED,
        ;

        public PaymentStatus toPaymentStatus() {
            return switch (this) {
                case PENDING -> PaymentStatus.UNKNOWN;
                case SUCCESS -> PaymentStatus.SUCCESS;
                case FAILED -> PaymentStatus.FAILED;
            };
        }
    }
}
