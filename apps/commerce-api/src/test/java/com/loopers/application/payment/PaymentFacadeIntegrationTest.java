package com.loopers.application.payment;

import static com.loopers.support.fixture.PaymentEventFixture.aPaymentEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentExcutor;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.PaymentEventJpaRepository;
import com.loopers.support.IntegrationTest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PaymentFacadeIntegrationTest extends IntegrationTest {

    private PaymentFacade paymentFacade;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentEventJpaRepository paymentEventJpaRepository;

    @MockitoBean
    private PaymentExcutor paymentExcutor;

    @BeforeEach
    void setUp() {
        paymentFacade = new PaymentFacade(paymentService, paymentExcutor);
    }

    @DisplayName("결제 승인 요청 시, ")
    @Nested
    class Create {
        @DisplayName("정상적인 결제 정보가 주어지면, 결제 이벤트는 실행중(EXECUTING) 상태로 변경된다.")
        @Test
        void createPaymentEvent_whenPaymentCreated() {
            // arrange
            PaymentEvent event = aPaymentEvent().build();
            paymentEventJpaRepository.save(event);
            PaymentCommand.Approve command = new PaymentCommand.Approve(
                "userId",
                event.getOrderId(),
                CardType.SAMSUNG,
                "1234-1234-1234-1234",
                event.getAmount(),
                PaymentMethod.CREDIT_CARD);

            when(paymentExcutor.request(command))
                .thenReturn(new PaymentRequestResult("transactionKey", PaymentStatus.SUCCESS, "success", true));

            // act
            paymentFacade.approve(command);

            // assert
            Optional<PaymentEvent> result = paymentEventJpaRepository.findById(event.getId());
            assertThat(result).isPresent();
            assertThat(result.get().getStatus()).isEqualTo(PaymentStatus.EXECUTING);
        }

        @DisplayName("결제 이벤트가 존재하지 않으면, 예외가 발생한다.")
        @Test
        void createPaymentEvent_whenPaymentEventNotFound() {
            // arrange
            PaymentCommand.Approve command = new PaymentCommand.Approve("userId", "orderId", CardType.SAMSUNG,
                "1234-1234-1234-1234", BigDecimal.valueOf(10000), PaymentMethod.CREDIT_CARD);

            // act & assert
            CoreException exception = assertThrows(CoreException.class, () -> {
                paymentFacade.approve(command);
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("결제 이벤트의 금액이 주어진 금액과 다르면, 예외가 발생한다.")
        @Test
        void createPaymentEvent_whenPaymentEventAmountIsNotEqual() {
            // arrange
            PaymentEvent event = aPaymentEvent().amount(BigDecimal.valueOf(10000)).build();
            paymentEventJpaRepository.save(event);

            // act & assert
            CoreException exception = assertThrows(CoreException.class, () -> {
                paymentFacade.approve(new PaymentCommand.Approve("userId",
                    "1234567890",
                    CardType.SAMSUNG,
                    "1234-1234-1234-1234",
                    BigDecimal.valueOf(100000),
                    PaymentMethod.CREDIT_CARD));
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }
}
