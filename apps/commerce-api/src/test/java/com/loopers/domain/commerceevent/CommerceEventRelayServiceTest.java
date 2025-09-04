package com.loopers.domain.commerceevent;

import static com.loopers.support.fixture.CommerceEventFixture.aCommerceEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.loopers.infrastructure.commerceevent.CommerceEventJpaRepository;
import com.loopers.support.IntegrationTest;

class CommerceEventRelayServiceTest extends IntegrationTest {
    @Autowired
    private CommerceEventRelayService commerceEventRelayService;

    @Autowired
    private CommerceEventJpaRepository commerceEventJpaRepository;

    @MockitoBean
    private CommerceEventPublisher commerceEventPublisher;

    @DisplayName("이벤트 발송 처리가 되지 않는 이벤트를 발송한다.")
    @Test
    void relayEvent_whenCommerceEventIsPending() {
        // arrange
        CommerceEvent event = aCommerceEvent().build();
        CommerceEvent event2 = aCommerceEvent().build();
        commerceEventJpaRepository.save(event);
        commerceEventJpaRepository.save(event2);

        // act
        commerceEventRelayService.relay();

        // assert
        verify(commerceEventPublisher, times(2)).publish(any());
    }
}
