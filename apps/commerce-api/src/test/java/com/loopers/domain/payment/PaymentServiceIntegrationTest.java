package com.loopers.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.IntegrationTest;

class PaymentServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentEventRepository paymentEventRepository;

    @DisplayName("결제 생성 시, ")
    @Nested
    class CreatePayment {
        @DisplayName("결제 이벤트가 생성된다.")
        @Test
        void createPayment_whenPaymentCreated() {
            // arrange
            PaymentCommand.Create command = new PaymentCommand.Create("ORD-1111-2222", BigDecimal.valueOf(10000));

            // act
            paymentService.create(command);

            // assert
            Optional<PaymentEvent> event = paymentEventRepository.findByOrderId(command.orderId());
            assertThat(event).isPresent();
            assertThat(event.get().getOrderId()).isEqualTo(command.orderId());
        }
    }
}
