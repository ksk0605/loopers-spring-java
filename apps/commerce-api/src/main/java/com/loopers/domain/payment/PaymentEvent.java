package com.loopers.domain.payment;

import static com.loopers.support.util.RequireUtils.requireNonEmpty;
import static com.loopers.support.util.RequireUtils.requireNotNull;

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
@Table(name = "payment_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerId;

    private String orderId;

    private boolean isPaymentDone;

    private String transactionKey;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;

    public PaymentEvent(String orderId, BigDecimal amount, String buyerId) {
        this.orderId = requireNonEmpty(orderId, "주문 번호는 필수 값 입니다.");
        this.amount = requireNotNull(amount, "결제 금액은 필수 값 입니다.");
        this.isPaymentDone = false;
        this.status = PaymentStatus.NOT_STARTED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.buyerId = buyerId;
    }

    public void execute() {
        if (status.isExcuted()) {
            throw new IllegalStateException("이미 실행 중인 결제입니다.");
        }
        status = PaymentStatus.EXECUTING;
        updatedAt = LocalDateTime.now();
    }

    public boolean isValid(BigDecimal amount) {
        return this.amount.compareTo(amount) == 0;
    }

    public void sync(PaymentCommand.Sync command) {
        this.transactionKey = command.transactionKey();
        this.amount = command.amount();
        this.status = command.status();
    }
}
