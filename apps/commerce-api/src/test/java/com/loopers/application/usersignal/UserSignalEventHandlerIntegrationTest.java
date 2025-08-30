package com.loopers.application.usersignal;

import static org.assertj.core.api.Assertions.assertThat;

import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loopers.domain.product.ProductViewedEvent;
import com.loopers.domain.usersignal.TargetType;
import com.loopers.domain.usersignal.UserSignal;
import com.loopers.infrastructure.usersignal.UserSignalJpaRepository;
import com.loopers.support.IntegrationTest;
import com.loopers.support.TestEventPulisher;
import static com.loopers.support.fixture.UserSignalFixture.anUserSignal;

public class UserSignalEventHandlerIntegrationTest extends IntegrationTest {

    @Autowired
    private TestEventPulisher testEventPulisher;

    @Autowired
    private UserSignalJpaRepository userSignalJpaRepository;

    @DisplayName("상품 조회 이벤트가 발행되면 비동기적으로 내부 이벤트로 변환 후 저장한다.")
    @Test
    void handleProductViewedEvent() {
        // arrange
        userSignalJpaRepository.save(anUserSignal().build());
        ProductViewedEvent productViewedEvent = new ProductViewedEvent(1L);

        // act
        testEventPulisher.publish(productViewedEvent);
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
            .until(() -> userSignalJpaRepository.findById(1L).isPresent());

        // assert
        UserSignal userSignal = userSignalJpaRepository.findById(1L).orElseThrow();
        assertThat(userSignal.getTargetType()).isEqualTo(TargetType.PRODUCT);
        assertThat(userSignal.getTargetId()).isEqualTo(1L);
        assertThat(userSignal.getViews()).isEqualTo(1);
    }
}
