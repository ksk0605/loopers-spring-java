package com.loopers.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.loopers.support.util.RequireUtils.requireNonEmpty;
import static com.loopers.support.util.RequireUtils.requireNotNull;

@Entity
@Table(name = "payment_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private boolean isPaymentDone;

    private String transactionKey;

    private PaymentMethod method;

    private PaymentStatus status;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;

    public PaymentEvent(String orderId, BigDecimal amount) {
        this.orderId = requireNonEmpty(orderId, "주문 번호는 필수 값 입니다.");
        this.amount = requireNotNull(amount, "결제 금액은 필수 값 입니다.");
        this.isPaymentDone = false;
        this.status = PaymentStatus.NOT_STARTED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
