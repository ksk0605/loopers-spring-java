package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class OrderTest {

    @DisplayName("주문을 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("올바른 정보들이 주어지면, 주문을 생성한다.")
        @Test
        void createOrder_whenValidInfoProvided() {
            // arrange
            Long userId = 1L;
            List<OrderItem> items = List.of(new OrderItem(1L, 1L, 1));

            // act
            Order order = new Order(userId, items);

            // assert
            assertAll(
                () -> assertThat(order.getUserId()).isEqualTo(userId),
                () -> assertThat(order.getItems()).isEqualTo(items)
            );
        }

        @DisplayName("유저 ID가 없으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createOrder_whenUserIdIsNull() {
            // arrange
            List<OrderItem> items = List.of(new OrderItem(1L, 1L, 1));

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Order(null, items)
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("주문 아이템이 비어있으면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createOrder_whenItemsIsEmpty() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Order(1L, List.of())
            );
            
            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("주문 아이템이 null이면, BAD REQUEST 예외를 발생시킨다.")
        @Test
        void createOrder_whenItemsIsNull() {
            // act
            CoreException result = assertThrows(CoreException.class,
                () -> new Order(1L, null)
            );
            
            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @DisplayName("주문을 결제처리할 때, ")
    @Nested
    class Pay {
        @DisplayName("주문이 결제 대기 중이라면, 주문 상태를 결제완료로 변경한다.")
        @Test
        void payOrder_whenOrderIsPendingPayment() {
            // arrange
            Order order = new Order(1L, List.of(new OrderItem(1L, 1L, 1)));

            // act
            order.pay();

            // assert
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PAYMENT_COMPLETED);
        }

        @DisplayName("주문이 결제 완료되어 있다면, 예외를 발생시킨다.")
        @Test
        void payOrder_whenOrderIsPaid() {
            // arrange
            Order order = new Order(1L, List.of(new OrderItem(1L, 1L, 1)));
            order.pay();

            // act
            CoreException result = assertThrows(CoreException.class,
                () -> order.pay()
            );

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
