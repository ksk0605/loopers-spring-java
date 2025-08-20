package com.loopers.application.payment;

import static com.loopers.support.fixture.PaymentEventFixture.aPaymentEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentAdapter;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentRequestResult;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentTransaction;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.infrastructure.payment.PaymentEventJpaRepository;
import com.loopers.infrastructure.payment.PaymentTransactionJpaRepository;
import com.loopers.support.IntegrationTest;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PaymentFacadeIntegrationTest extends IntegrationTest {

    private PaymentFacade paymentFacade;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentEventJpaRepository paymentEventJpaRepository;

    @Autowired
    private PaymentTransactionJpaRepository paymentTransactionJpaRepository;

    @MockitoBean
    private PaymentAdapter paymentAdapter;

    @BeforeEach
    void setUp() {
        paymentFacade = new PaymentFacade(paymentService, paymentAdapter);
    }

    @DisplayName("결제 승인 요청 시, ")
    @Nested
    class Request {
        @DisplayName("정상적인 결제 정보가 주어지면, 결제 이벤트는 실행중(EXECUTING) 상태로 변경된다.")
        @Test
        void createPaymentEvent_whenPaymentCreated() {
            // arrange
            PaymentEvent event = aPaymentEvent().build();
            paymentEventJpaRepository.save(event);
            PaymentCommand.Request command = new PaymentCommand.Request(
                "userId",
                event.getOrderId(),
                CardType.SAMSUNG,
                "1234-1234-1234-1234",
                event.getAmount(),
                PaymentMethod.CREDIT_CARD);

            when(paymentAdapter.request(command))
                .thenReturn(new PaymentRequestResult("transactionKey", PaymentStatus.SUCCESS, "success", true));

            // act
            paymentFacade.requestPayment(command);

            // assert
            Optional<PaymentEvent> result = paymentEventJpaRepository.findById(event.getId());
            assertThat(result).isPresent();
            assertThat(result.get().getStatus()).isEqualTo(PaymentStatus.EXECUTING);
        }

        @DisplayName("결제 이벤트가 존재하지 않으면, 예외가 발생한다.")
        @Test
        void createPaymentEvent_whenPaymentEventNotFound() {
            // arrange
            PaymentCommand.Request command = new PaymentCommand.Request("userId", "orderId", CardType.SAMSUNG,
                "1234-1234-1234-1234", BigDecimal.valueOf(10000), PaymentMethod.CREDIT_CARD);

            // act & assert
            CoreException exception = assertThrows(CoreException.class, () -> {
                paymentFacade.requestPayment(command);
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
                paymentFacade.requestPayment(new PaymentCommand.Request("userId",
                    "1234567890",
                    CardType.SAMSUNG,
                    "1234-1234-1234-1234",
                    BigDecimal.valueOf(100000),
                    PaymentMethod.CREDIT_CARD));
            });

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }

    @DisplayName("결제 싱크를 시도할 때, ")
    @Nested
    class Sync {
        @DisplayName("주어진 정보를 바탕으로 결제 이벤트 정보를 업데이트하고, 결제 이벤트에 대한 결제 트랜잭션을 생성한다.")
        @Test
        void syncPayment_whenValidTransactionInfoProvided() {
            // arrange
            PaymentEvent event = paymentEventJpaRepository.save(aPaymentEvent().build());

            // act
            paymentFacade.syncPayment(new PaymentCommand.Sync(
                event.getOrderId(),
                "key-12345",
                CardType.SAMSUNG,
                "1234-1234-1234-1234",
                event.getAmount(),
                PaymentStatus.SUCCESS,
                null
            ));

            // assert
            Optional<PaymentTransaction> transaction = paymentTransactionJpaRepository.findById(1L);
            assertThat(transaction).isPresent();
            assertThat(transaction.get().getOrderId()).isEqualTo(event.getOrderId());
            assertThat(transaction.get().getTransactionKey()).isEqualTo("key-12345");
            assertThat(transaction.get().getStatus()).isEqualTo(PaymentStatus.SUCCESS);

            Optional<PaymentEvent> updatedEvent = paymentEventJpaRepository.findById(event.getId());
            assertThat(updatedEvent).isPresent();
            assertThat(updatedEvent.get().getStatus()).isEqualTo(PaymentStatus.SUCCESS);
            assertThat(updatedEvent.get().getTransactionKey()).isEqualTo("key-12345");
            assertThat(updatedEvent.get().getAmount()).isEqualByComparingTo(event.getAmount());
            assertThat(updatedEvent.get().getApprovedAt()).isNotNull();
        }

        @DisplayName("팬딩상태(EXECUTING,UNKNOWN) 결제를 모두 싱크한다.")
        @Test
        void syncAllPendingPayments() {
            // arrange
            PaymentEvent successEvent = aPaymentEvent().orderId("1234").build();
            successEvent.success();
            paymentEventJpaRepository.save(successEvent);
            PaymentEvent executingEvent = aPaymentEvent().orderId("5678").build();
            executingEvent.execute();
            paymentEventJpaRepository.save(executingEvent);

            when(paymentAdapter.getTransaction(any(), any())).
                thenReturn(new TransactionInfo("key-12345", "5678", CardType.SAMSUNG, "1234-1234-1234-1234", BigDecimal.valueOf(10000), PaymentStatus.SUCCESS, null));

            // act
            paymentFacade.syncPayments();

            // assert
            List<PaymentEvent> events = paymentEventJpaRepository.findAll();
            assertThat(events).hasSize(2);
            assertThat(events.get(0).getStatus()).isEqualTo(PaymentStatus.SUCCESS);
            assertThat(events.get(1).getStatus()).isEqualTo(PaymentStatus.SUCCESS);

            List<PaymentTransaction> transactions = paymentTransactionJpaRepository.findAll();
            assertThat(transactions).hasSize(1);
        }
    }
}
