package com.loopers.application.event;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static org.assertj.core.api.Assertions.assertThat;

import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loopers.domain.event.InternalEvent;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCreatedEvent;
import com.loopers.infrastructure.event.InternalEventJpaRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.support.IntegrationTest;
import com.loopers.support.TestEventPulisher;

public class LoggableEventHandlerIntegrationTest extends IntegrationTest {

    @Autowired
    private TestEventPulisher testEventPulisher;

    @Autowired
    private InternalEventJpaRepository internalEventJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @DisplayName("로깅할 수 있는 이벤트가 발행되면 비동기적으로 내부 이벤트로 변환 후 저장한다.")
    @Test
    void saveInternalEvent_whenLoggableEventIsPublished() {
        // arrange
        Order order = orderJpaRepository.save(anOrder().build());
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order, 1L, "test");

        // act
        testEventPulisher.publish(orderCreatedEvent);
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
            .until(() -> internalEventJpaRepository.findById(1L).isPresent());

        // assert
        InternalEvent internalEvent = internalEventJpaRepository.findById(1L).orElseThrow();
        assertThat(internalEvent.getEventType()).isEqualTo(orderCreatedEvent.getClass().getSimpleName());
        assertThat(internalEvent.getAttributes()).isNotNull();
        assertThat(internalEvent.getCreatedAt()).isNotNull();
    }
}
