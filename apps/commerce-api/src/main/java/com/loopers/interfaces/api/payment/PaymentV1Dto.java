package com.loopers.interfaces.api.payment;

import java.math.BigDecimal;

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
        public PaymentCommand.Approve toCommand(String userId) {
            return new PaymentCommand.Approve(
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
        String transactionKey,
        String status
    ) {
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
        CREDIT_CARD_AND_POINT,
        ;

        public PaymentMethod toPaymentMethod() {
            return switch (this) {
                case CREDIT_CARD -> PaymentMethod.CREDIT_CARD;
                case POINT -> PaymentMethod.POINT;
                case CREDIT_CARD_AND_POINT -> PaymentMethod.CREDIT_CARD_AND_POINT;
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
        public PaymentCommand.Callback toCommand() {
            return new PaymentCommand.Callback(
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
