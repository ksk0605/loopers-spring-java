package com.loopers.domain.payment;

import static com.loopers.support.util.RequireUtils.requireNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private BigDecimal amount;

    private LocalDateTime paymentDate;

    public Payment(Long orderId, PaymentMethod method, PaymentStatus status, BigDecimal amount) {
        this.orderId = requireNotNull(orderId, "주문 ID는 필수입니다.");
        this.method = requireNotNull(method, "결제 방법은 필수입니다.");
        this.status = requireNotNull(status, "결제 상태는 필수입니다.");
        this.amount = requireNotNull(amount, "결제 금액은 필수입니다.");
        this.paymentDate = LocalDateTime.now();
    }

    public void process(PaymentValidationStrategy validator) {
        validator.validate(this);
        this.status = PaymentStatus.COMPLETED;
        this.paymentDate = LocalDateTime.now();
    }

    public boolean isAvailable(BigDecimal paymentAmount) {
        return this.status == PaymentStatus.PENDING && this.amount.compareTo(paymentAmount) <= 0;
    }
}
