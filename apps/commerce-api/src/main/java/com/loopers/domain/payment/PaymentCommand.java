package com.loopers.domain.payment;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Create(
        String orderId,
        BigDecimal amount,
        Long userId,
        String username
    ) {
    }

    public record Request(
        String userId,
        String orderId,
        CardType cardType,
        String cardNo,
        BigDecimal amount,
        PaymentMethod method
    ) {
    }

    public record Sync(
        String orderId,
        String transactionKey,
        CardType cardType,
        String cardNo,
        BigDecimal amount,
        PaymentStatus status,
        String reason
    ) {
        public static Sync from(TransactionInfo info) {
            return new Sync(
                info.orderId(),
                info.transactionKey(),
                info.cardType(),
                info.cardNo(),
                info.amount(),
                info.status(),
                info.reason()
            );
        }

        public boolean success() {
            return status == PaymentStatus.SUCCESS;
        }

        public boolean fail() {
            return status == PaymentStatus.FAILED;
        }
    }
}
