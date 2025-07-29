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
    }
}
