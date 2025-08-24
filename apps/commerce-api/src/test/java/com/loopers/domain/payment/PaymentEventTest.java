package com.loopers.domain.payment;

import static com.loopers.support.fixture.PaymentEventFixture.aPaymentEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PaymentEventTest {

    @DisplayName("결제 이벤트 생성 시, ")
    @Nested
    class Create {
        @DisplayName("주문 번호와 결제 금액이 주어지면, 결제 이벤트가 생성된다.")
        @Test
        void createPaymentEvent_whenOrderIdAndAmountAreProvided() {
            // arrange
            String orderId = "ORD-1111-2222";
            BigDecimal amount = BigDecimal.valueOf(10000);

            // act
            PaymentEvent paymentEvent = new PaymentEvent(orderId, amount, "userId");

            // assert
            assertThat(paymentEvent.getOrderId()).isEqualTo(orderId);
            assertThat(paymentEvent.getAmount()).isEqualTo(amount);
            assertThat(paymentEvent.getStatus()).isEqualTo(PaymentStatus.NOT_STARTED);
            assertThat(paymentEvent.getCreatedAt()).isNotNull();
            assertThat(paymentEvent.getUpdatedAt()).isNotNull();
            assertThat(paymentEvent.getApprovedAt()).isNull();
        }

        @DisplayName("주문 번호가 없으면, 예외를 발생시킨다.")
        @Test
        void createPaymentEvent_whenOrderIdIsEmpty() {
            // act & assert
            assertThrows(IllegalArgumentException.class, () -> new PaymentEvent(null, BigDecimal.valueOf(10000), "userId"));
        }

        @DisplayName("결제 금액이 없으면, 예외를 발생시킨다.")
        @Test
        void createPaymentEvent_whenAmountIsEmpty() {
            // act & assert
            assertThrows(IllegalArgumentException.class, () -> new PaymentEvent("ORD-1111-2222", null, "userId"));
        }
    }

    @DisplayName("결제를 실행할 때, ")
    @Nested
    class Execute {
        @DisplayName("이미 성공한 결제를 실행하면, 예외를 발생시킨다.")
        @Test
        void throwsException_whenAlreadySuccess() {
            // arrange
            PaymentEvent event = aPaymentEvent().build();
            event.success();

            // act & assert
            assertThrows(IllegalStateException.class, () -> event.execute());
        }

        @DisplayName("이미 실패한 결제를 실행하면, 예외를 발생시킨다.")
        @Test
        void throwsException_whenAlreadyFail() {
            // arrange
            PaymentEvent event = aPaymentEvent().build();
            event.fail();

            // act & assert
            assertThrows(IllegalStateException.class, () -> event.execute());
        }
    }
}
