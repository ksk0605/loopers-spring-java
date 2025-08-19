package com.loopers.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private String transactionKey;

    private CardType method;

    private BigDecimal amount;

    private PaymentStatus status;

    private Long paymentEventId;

    private LocalDateTime createdAt;
}
