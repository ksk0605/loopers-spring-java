package com.loopers.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PaymentTest {

    @DisplayName("결제 생성 시, ")
    @Nested
    class Create {
        @DisplayName("올바른 정보들이 주어지면, 결제를 생성한다.")
        @Test
        void createPayment_whenValidInfoProvided() {
            // arrange
            Long orderId = 1L;
            PaymentMethod method = PaymentMethod.CREDIT_CARD;
            PaymentStatus status = PaymentStatus.PENDING;
            BigDecimal amount = BigDecimal.valueOf(10000);

            // act
            Payment payment = new Payment(orderId, method, status, amount);

            // assert
            assertAll(
                () -> assertThat(payment.getOrderId()).isEqualTo(orderId),
                () -> assertThat(payment.getMethod()).isEqualTo(method),
                () -> assertThat(payment.getStatus()).isEqualTo(status),
                () -> assertThat(payment.getAmount()).isEqualTo(amount),
                () -> assertThat(payment.getPaymentDate()).isNotNull()
            );
        }
    }
}
