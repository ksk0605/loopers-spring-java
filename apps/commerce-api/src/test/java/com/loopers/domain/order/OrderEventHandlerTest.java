package com.loopers.domain.order;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loopers.domain.payment.PaymentSuccessEvent;

@ExtendWith(MockitoExtension.class)
class OrderEventHandlerTest {

    private OrderEventHandler orderEventHandler;

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @BeforeEach
    void setUp() {
        orderEventHandler = new OrderEventHandler(orderService, orderEventPublisher);
    }

    @Test
    @DisplayName("PaymentSuccessEvent를 수신하면, 주문 상태를 '결제완료'로 변경하고 OrderPaid 이벤트를 발행해야 한다.")
    void handlePaymentSuccess_shouldUpdateOrderAndPublishEvent() {
        // arrange
        Order order = anOrder().build();
        PaymentSuccessEvent event = new PaymentSuccessEvent(order.getOrderId());

        when(orderRepository.find(order.getOrderId())).thenReturn(Optional.of(order));

        // act
        orderEventHandler.handlePaymentSuccess(event);

        // assert
        verify(orderRepository, times(1)).find(order.getOrderId());
        verify(orderEventPublisher, times(1)).publishOrderPaid(order);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
    }
}
