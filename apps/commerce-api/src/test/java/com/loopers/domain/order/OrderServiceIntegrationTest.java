package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("주문 목록을 조회 할 때, ")
    @Nested
    class GetAll {
        @DisplayName("올바른 유저 ID가 주어지면, 주문 목록을 조회 할 수 있다.")
        @Test
        void returnOrders_whenValidUserId() {
            // arrange
            orderJpaRepository.save(new Order(1L, List.of(new OrderItem(1L, 1L, 10))));
            orderJpaRepository.save(new Order(1L, List.of(new OrderItem(1L, 1L, 5))));
            orderJpaRepository.save(new Order(1L, List.of(new OrderItem(1L, 1L, 20))));

            // act
            List<OrderInfo> orders = orderService.getAll(1L);

            // assert
            assertAll(
                () -> assertThat(orders).hasSize(3),
                () -> assertThat(orders.get(0).userId()).isEqualTo(1L),
                () -> assertThat(orders.get(0).status()).isEqualTo(OrderStatus.PENDING),
                () -> assertThat(orders.get(1).userId()).isEqualTo(1L),
                () -> assertThat(orders.get(1).status()).isEqualTo(OrderStatus.PENDING),
                () -> assertThat(orders.get(2).userId()).isEqualTo(1L),
                () -> assertThat(orders.get(2).status()).isEqualTo(OrderStatus.PENDING)
            );
        }
    }
}
