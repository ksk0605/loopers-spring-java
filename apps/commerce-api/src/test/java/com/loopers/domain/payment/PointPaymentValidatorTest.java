package com.loopers.domain.payment;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PointPaymentValidatorTest {
    private PointPaymentValidator validator;
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        validator = new PointPaymentValidator(orderRepository, userRepository);
    }

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
                payment.process(validator);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("이미 결제된 주문이면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void validatePayment_whenOrderIsAlreadyPaid() {
            // arrange
            Payment payment = new Payment(1L, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING,
                BigDecimal.valueOf(1000));

            Order order = anOrder().build();
            order.pay();
            User user = new User("userId", Gender.MALE, "1990-01-01", "test@test.com");
            user.chargePoint(1000);
            when(orderRepository.find(1L)).thenReturn(Optional.of(order));
            when(userRepository.find(1L)).thenReturn(Optional.of(user));

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                validator.validate(payment);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("결제 금액보다 포인트가 적으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void validatePayment_whenPointIsLessThanAmount() {
            // arrange
            Payment payment = new Payment(1L, PaymentMethod.CREDIT_CARD, PaymentStatus.PENDING,
                BigDecimal.valueOf(1000));
            Order order = anOrder().build();
            User user = new User("userId", Gender.MALE, "1990-01-01", "test@test.com");
            when(orderRepository.find(1L)).thenReturn(Optional.of(order));
            when(userRepository.find(1L)).thenReturn(Optional.of(user));

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                validator.validate(payment);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
