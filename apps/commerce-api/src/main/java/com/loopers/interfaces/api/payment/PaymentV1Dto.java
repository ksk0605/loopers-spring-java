package com.loopers.interfaces.api.payment;

import java.math.BigDecimal;

import com.loopers.application.payment.PaymentResult;
import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentStatus;

public class PaymentV1Dto {
    public record PaymentRequest(
        String orderId,
        CardTypeDto cardType,
        String cardNo,
        Long amount,
        PaymentMethodDto method
    ) {
        public PaymentCommand.Request toCommand(String userId) {
            return new PaymentCommand.Request(
                userId,
                orderId,
                cardType.toCardType(),
                cardNo,
                BigDecimal.valueOf(amount),
                method.toPaymentMethod()
            );
        }
    }

    public record PaymentResponse(
        String transactionKey
    ) {
        public static PaymentResponse from(PaymentResult result) {
            return new PaymentResponse(
                result.transactionKey()
            );
        }
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

    public enum PaymentMethodDto {
        CREDIT_CARD,
        POINT,
        ;

        public PaymentMethod toPaymentMethod() {
            return switch (this) {
                case CREDIT_CARD -> PaymentMethod.CREDIT_CARD;
                case POINT -> PaymentMethod.POINT;
            };
        }
    }

    public record PaymentCallbackRequest(
        String transactionKey,
        String orderId,
        CardTypeDto cardType,
        String cardNo,
        Long amount,
        PaymentStatusDto status,
        String reason
    ) {

        public PaymentCommand.Sync toCommand() {
            return new PaymentCommand.Sync(
                orderId,
                transactionKey,
                cardType.toCardType(),
                cardNo,
                BigDecimal.valueOf(amount),
                status.toPaymentStatus(),
                reason
            );
        }
    }

    public enum PaymentStatusDto {
        PENDING,
        SUCCESS,
        FAILED,
        ;

        public PaymentStatus toPaymentStatus() {
            return switch (this) {
                case PENDING -> PaymentStatus.EXECUTING;
                case SUCCESS -> PaymentStatus.SUCCESS;
                case FAILED -> PaymentStatus.FAILED;
            };
        }
    }
}
