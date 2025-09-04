package com.loopers.domain.commerceevent;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loopers.infrastructure.commerceevent.CommerceEventJpaRepository;
import com.loopers.support.IntegrationTest;

public class CommerceEventOutboxingServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private CommerceEventOutboxingService commerceEventOutboxingService;

    @Autowired
    private CommerceEventJpaRepository commerceEventJpaRepository;

    @DisplayName("CommerceEvent를 로깅하면, 멱등성 처리를 위한 eventId, 파티션 기반 제어를 위한 aggregateId, 이벤트 타입, payload가 저장된다.")
    @Test
    void recordCommerceEvent() {
        // arrange
        CommerceEventCommand.Record command = new CommerceEventCommand.Record("aaa-bbb-ccc-123", "test", "test", null);

        // act
        commerceEventOutboxingService.record(command);

        // assert
        CommerceEvent commerceEvent = commerceEventJpaRepository.findById(1L).get();
        assertThat(commerceEvent).isNotNull();
        assertThat(commerceEvent.getEventId()).isNotNull();
        assertThat(commerceEvent.getEventType()).isEqualTo(command.eventType());
        assertThat(commerceEvent.getAggregateId()).isEqualTo(command.aggregateId());
        assertThat(commerceEvent.getPayload()).isEqualTo(command.payload());
        assertThat(commerceEvent.getCreatedAt()).isNotNull();
        assertThat(commerceEvent.getRetryCount()).isEqualTo(0);
    }
}
