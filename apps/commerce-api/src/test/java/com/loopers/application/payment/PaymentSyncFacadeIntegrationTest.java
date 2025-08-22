package com.loopers.application.payment;

import static com.loopers.support.fixture.PaymentEventFixture.aPaymentEvent;
import static org.assertj.core.api.Assertions.assertThat;
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
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.payment.PaymentTransaction;
import com.loopers.domain.payment.TransactionInfo;
import com.loopers.infrastructure.payment.PaymentEventJpaRepository;
import com.loopers.infrastructure.payment.PaymentTransactionJpaRepository;
import com.loopers.support.IntegrationTest;

class PaymentSyncFacadeIntegrationTest extends IntegrationTest {
    private PaymentSyncFacade paymentSyncFacade;

    @Autowired
    private PaymentEventJpaRepository paymentEventJpaRepository;

    @Autowired
    private PaymentService paymentService;

    @MockitoBean
    private PaymentAdapter paymentAdapter;

    @Autowired
    private PaymentTransactionJpaRepository paymentTransactionJpaRepository;

    @BeforeEach
    void setUp() {
        paymentSyncFacade = new PaymentSyncFacade(paymentService, paymentAdapter);
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
            paymentSyncFacade.syncPayment(new PaymentCommand.Sync(
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
            paymentSyncFacade.syncPayments();

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
