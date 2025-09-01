package com.loopers.application.event;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.loopers.application.coupon.CouponEventHandler;
import com.loopers.application.inventory.InventoryEventHandler;
import com.loopers.domain.event.InternalEvent;
import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.like.UnlikeEvent;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCreatedEvent;
import com.loopers.domain.product.ProductViewedEvent;
import com.loopers.infrastructure.event.InternalEventJpaRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.support.IntegrationTest;
import com.loopers.support.TestEventPublisher;

public class LoggableEventHandlerIntegrationTest extends IntegrationTest {

    @Autowired
    private TestEventPublisher testEventPublisher;

    @Autowired
    private InternalEventJpaRepository internalEventJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @MockitoBean
    private CouponEventHandler couponEventHandler;

    @MockitoBean
    private InventoryEventHandler inventoryEventHandler;

    @DisplayName("주문 생성 이벤트가 발행되면 비동기적으로 내부 이벤트로 변환 후 저장한다.")
    @Test
    void saveInternalEvent_whenLoggableEventIsPublished() {
        // arrange
        Order order = orderJpaRepository.save(anOrder().build());
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order, 1L, "test");
        doNothing().when(couponEventHandler).handleOrderCreated(any());
        doNothing().when(inventoryEventHandler).handleOrderCreated(any());

        // act
        testEventPublisher.publish(orderCreatedEvent);
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

    @DisplayName("좋아요 이벤트가 발행되면 비동기적으로 내부 이벤트로 변환 후 저장한다.")
    @Test
    void saveInternalEvent_whenLikeEventIsPublished() {
        // arrange
        LikeEvent likeEvent = new LikeEvent(new LikeTarget(1L, LikeTargetType.PRODUCT));

        // act
        testEventPublisher.publish(likeEvent);
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
            .until(() -> internalEventJpaRepository.findById(1L).isPresent());

        // assert
        InternalEvent internalEvent = internalEventJpaRepository.findById(1L).orElseThrow();
        assertThat(internalEvent.getEventType()).isEqualTo(likeEvent.getClass().getSimpleName());
        assertThat(internalEvent.getAttributes()).isNotNull();
        assertThat(internalEvent.getCreatedAt()).isNotNull();
        assertThat(internalEvent.getAttributes()).containsEntry("targetId", 1);
        assertThat(internalEvent.getAttributes()).containsEntry("targetType", "PRODUCT");
    }

    @DisplayName("좋아요 취소 이벤트가 발행되면 비동기적으로 내부 이벤트로 변환 후 저장한다.")
    @Test
    void saveInternalEvent_whenUnlikeEventIsPublished() {
        // arrange
        UnlikeEvent unlikeEvent = new UnlikeEvent(new LikeTarget(1L, LikeTargetType.PRODUCT));

        // act
        testEventPublisher.publish(unlikeEvent);
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
            .until(() -> internalEventJpaRepository.findById(1L).isPresent());

        // assert
        InternalEvent internalEvent = internalEventJpaRepository.findById(1L).orElseThrow();
        assertThat(internalEvent.getEventType()).isEqualTo(unlikeEvent.getClass().getSimpleName());
        assertThat(internalEvent.getAttributes()).isNotNull();
        assertThat(internalEvent.getCreatedAt()).isNotNull();
        assertThat(internalEvent.getAttributes()).containsEntry("targetId", 1);
        assertThat(internalEvent.getAttributes()).containsEntry("targetType", "PRODUCT");
    }

    @DisplayName("상품 조회 이벤트가 발행되면 비동기적으로 내부 이벤트로 변환 후 저장한다.")
    @Test
    void saveInternalEvent_whenProductViewedEventIsPublished() {
        // arrange
        ProductViewedEvent productViewedEvent = new ProductViewedEvent(1L);

        // act
        testEventPublisher.publish(productViewedEvent);
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
            .until(() -> internalEventJpaRepository.findById(1L).isPresent());

        // assert
        InternalEvent internalEvent = internalEventJpaRepository.findById(1L).orElseThrow();
        assertThat(internalEvent.getEventType()).isEqualTo(productViewedEvent.getClass().getSimpleName());
        assertThat(internalEvent.getAttributes()).isNotNull();
        assertThat(internalEvent.getCreatedAt()).isNotNull();
        assertThat(internalEvent.getAttributes()).containsEntry("productId", 1);
    }
}
