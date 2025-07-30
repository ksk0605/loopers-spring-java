package com.loopers.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
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
    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime paymentDate;

    public Payment(Long orderId, PaymentMethod method, PaymentStatus status, BigDecimal amount) {
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
    }

    public void process(PaymentValidator validator) {
        validator.validate(this);
        this.status = PaymentStatus.COMPLETED;
        this.paymentDate = LocalDateTime.now();
    }

    public boolean isAvailable(BigDecimal paymentAmount) {
        return this.status == PaymentStatus.PENDING && this.amount.compareTo(paymentAmount) <= 0;
    }
}
