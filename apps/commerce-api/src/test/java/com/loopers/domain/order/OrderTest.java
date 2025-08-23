package com.loopers.domain.order;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @DisplayName("주문을 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("올바른 정보들이 주어지면, 주문을 생성한다.")
        @Test
        void createOrder_whenValidInfoProvided() {
            // arrange
            Long userId = 1L;
            List<OrderItem> items = List.of(new OrderItem(1L, 1L, 1, BigDecimal.valueOf(20000), BigDecimal.valueOf(1000)));

            // act
            Order order = new Order(userId, items);

            // assert
            assertAll(
                () -> assertThat(order.getUserId()).isEqualTo(userId),
                () -> assertThat(order.getItems()).isEqualTo(items),
                () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING)
            );
        }

        @DisplayName("유저 ID가 없으면, 예외를 발생시킨다.")
        @Test
        void createOrder_whenUserIdIsNull() {
            // arrange
            List<OrderItem> items = List.of(new OrderItem(1L, 1L, 1, BigDecimal.valueOf(20000), BigDecimal.valueOf(1000)));

            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Order(null, items)
            );
        }

        @DisplayName("주문 아이템이 비어있으면, 예외를 발생시킨다.")
        @Test
        void createOrder_whenItemsIsEmpty() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Order(1L, List.of())
            );
        }

        @DisplayName("주문 아이템이 null이면, 예외를 발생시킨다.")
        @Test
        void createOrder_whenItemsIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new Order(1L, null)
            );
        }

        @DisplayName("주문 아이템의 상품 ID가 null이면, 예외를 발생시킨다.")
        @Test
        void createOrder_whenProductIdIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new OrderItem(null, 1L, 1, BigDecimal.valueOf(20000), BigDecimal.valueOf(1000))
            );
        }

        @DisplayName("주문 아이템의 상품 옵션 ID가 null이면, 예외를 발생시킨다.")
        @Test
        void createOrder_whenProductOptionIdIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new OrderItem(1L, null, 1, BigDecimal.valueOf(20000), BigDecimal.valueOf(1000))
            );
        }

        @DisplayName("주문 아이템의 수량이 null이면, 예외를 발생시킨다.")
        @Test
        void createOrder_whenQuantityIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class,
                () -> new OrderItem(1L, 1L, null, BigDecimal.valueOf(20000), BigDecimal.valueOf(1000))
            );
        }
    }

    @DisplayName("주문을 결제처리할 때, ")
    @Nested
    class Pay {
        @DisplayName("주문이 결제 대기 중이라면, 주문 상태를 결제완료로 변경한다.")
        @Test
        void payOrder_whenOrderIsPendingPayment() {
            // arrange
            Order order = anOrder().build();

            // act
            order.completePayment();

            // assert
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
        }

        @DisplayName("주문이 결제 완료되어 있다면, 예외를 발생시킨다.")
        @Test
        void payOrder_whenOrderIsPaid() {
            // arrange
            Order order = anOrder().build();
            order.completePayment();

            // act & assert
            assertThrows(IllegalStateException.class,
                () -> order.completePayment()
            );
        }
    }

    @DisplayName("주문의 총 가격을 계산할 수 있다.")
    @Test
    void getTotalPrice() {
        // arrange
        Order order = anOrder().build();

        // act
        BigDecimal price = order.getTotalPrice();

        // assert
        assertThat(price).isEqualTo(BigDecimal.valueOf(11000));
    }
}
