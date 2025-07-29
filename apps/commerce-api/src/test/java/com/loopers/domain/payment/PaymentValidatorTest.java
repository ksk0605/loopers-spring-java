package com.loopers.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PaymentValidatorTest {
    private PaymentValidator paymentValidator = new PaymentValidator();

    @DisplayName("결제 유효성 검사 시, ")
    @Nested
    class Validate {
        @DisplayName("결제 금액이 0원 미만이면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void validatePayment_whenAmountIsLessThanZero() {
            // arrange
            Payment payment = new Payment(1L, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING, BigDecimal.valueOf(-1));

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                payment.process(paymentValidator);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
