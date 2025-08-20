package com.loopers.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String transactionKey;

    @Enumerated(EnumType.STRING)
    private CardType method;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Long paymentEventId;

    private LocalDateTime createdAt;

    public PaymentTransaction(String orderId, String transactionKey, CardType method, BigDecimal amount,
        PaymentStatus status, Long paymentEventId) {
        this.orderId = orderId;
        this.transactionKey = transactionKey;
        this.method = method;
        this.amount = amount;
        this.status = status;
        this.paymentEventId = paymentEventId;
        this.createdAt = LocalDateTime.now();
    }

    public static PaymentTransaction of(PaymentCommand.Sync command, Long paymentEventId) {
        return new PaymentTransaction(
            command.orderId(),
            command.transactionKey(),
            command.cardType(),
            command.amount(),
            command.status(),
            paymentEventId
        );
    }
}
