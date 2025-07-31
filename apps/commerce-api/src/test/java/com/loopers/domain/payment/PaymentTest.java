package com.loopers.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;


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

        @DisplayName("주문 ID가 없으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createPayment_whenOrderIdIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Payment(null, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, BigDecimal.valueOf(10000))
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("결제 방법이 없으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createPayment_whenMethodIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Payment(1L, null, PaymentStatus.PENDING, BigDecimal.valueOf(10000))
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("결제 상태가 없으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createPayment_whenStatusIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Payment(1L, PaymentMethod.CREDIT_CARD, null, BigDecimal.valueOf(10000))
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("결제 금액이 없으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createPayment_whenAmountIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Payment(1L, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, null)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
