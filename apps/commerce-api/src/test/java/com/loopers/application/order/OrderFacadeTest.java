package com.loopers.application.order;

import static com.loopers.support.fixture.InventoryFixture.anInventory;
import static com.loopers.support.fixture.ProductFixture.aProduct;
import static com.loopers.support.fixture.ProductOptionFixture.aProductOption;
import static com.loopers.support.fixture.UserFixture.anUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductOption;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.inventory.InventoryJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.IntegrationTest;

class OrderFacadeTest extends IntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @DisplayName("주문 정보가 정상적으로 주어지면, 주문 결과를 반환한다.")
    @Test
    void order() {
        // arrange
        User user = anUser().build();
        user.updatePoint(1000000);
        userJpaRepository.save(user);

        Product product = aProduct().name("상품 1").build();
        ProductOption productOption = aProductOption().build();
        product.addOption(productOption);
        productJpaRepository.save(product);

        Inventory inventory = anInventory().build();
        inventoryJpaRepository.save(inventory);

        // act
        OrderCriteria.Order cri = new OrderCriteria.Order(1L, List.of(new OrderCriteria.Item(1L, 1L, 10)));
        OrderResult result = orderFacade.order(cri);

        // assert
        assertAll(
            () -> assertThat(result.userId()).isEqualTo(1L),
            () -> assertThat(result.totalPrice()).isEqualByComparingTo(BigDecimal.valueOf(210000)), // (기본20000원 + 옵션 추가1000원) * 10개
            () -> assertThat(result.status()).isEqualTo(OrderStatus.PAYMENT_COMPLETED),
            () -> assertThat(result.id()).isEqualTo(1L),
            () -> assertThat(result.paymentStatus()).isEqualTo(PaymentStatus.COMPLETED)
        );
    }

    @DisplayName("동일한 상품에 대해 동시에 주문을 요청할 때, ")
    @Nested
    class Concurrency {
        @DisplayName("두개의 주문 중 한 주문에 의해 재고가 부족해지는 경우, 하나만 성공하고 다른 주문은 실패한다")
        @Test
        void shouldPreventOverselling_whenConcurrentOrders() throws InterruptedException {
            // arrange
            User user1 = anUser().userId("testUser1").build();
            user1.updatePoint(1000000);
            userJpaRepository.save(user1);
            User user2 = anUser().userId("testUser2").build();
            user2.updatePoint(1000000);
            userJpaRepository.save(user2);

            Product product = aProduct().build();
            ProductOption productOption = aProductOption().build();
            product.addOption(productOption);
            productJpaRepository.save(product);

            int initialStock = 5;
            inventoryJpaRepository.save(anInventory().quantity(initialStock).build());

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch countDownLatch = new CountDownLatch(2);

            // act
            List<Future<OrderResult>> futures = new ArrayList<>();

            // 첫 번째 주문 (4개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.order(new OrderCriteria.Order(1L, List.of(new OrderCriteria.Item(1L, 1L, 4))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 두 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.order(new OrderCriteria.Order(2L, List.of(new OrderCriteria.Item(1L, 1L, 4))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            countDownLatch.await();

            // assert
            long successfulOrders = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .count();

            Optional<Inventory> savedInv = inventoryJpaRepository.findById(1L);
            Optional<User> savedUser1 = userJpaRepository.findById(1L);
            Optional<User> savedUser2 = userJpaRepository.findById(2L);
            assertThat(savedInv.isPresent()).isTrue();
            assertThat(savedUser1.isPresent()).isTrue();
            assertThat(savedUser2.isPresent()).isTrue();
            assertAll(
                () -> assertThat(successfulOrders).isEqualTo(1),
                () -> assertThat(savedInv.get().getQuantity()).isEqualTo(1)
            );
        }

        @DisplayName("재고가 충분하다면, 모든 주문을 정상적으로 완료시킨다.")
        @Test
        void completeAllOrders_whenSufficientStock() throws InterruptedException {
            User user1 = anUser().userId("testUser1").build();
            user1.updatePoint(6000000);
            userJpaRepository.save(user1);
            User user2 = anUser().userId("testUser2").build();
            user2.updatePoint(6000000);
            userJpaRepository.save(user2);
            User user3 = anUser().userId("testUser3").build();
            user3.updatePoint(6000000);
            userJpaRepository.save(user3);

            Product product = aProduct().build();
            ProductOption productOption = aProductOption().build();
            product.addOption(productOption);
            productJpaRepository.save(product);

            int initialStock = 10;
            Inventory inventory = new Inventory(1L, 1L, initialStock);
            inventoryJpaRepository.save(inventory);

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch countDownLatch = new CountDownLatch(2);

            // act
            List<Future<OrderResult>> futures = new ArrayList<>();

            // 첫 번째 주문 (4개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.order(new OrderCriteria.Order(1L, List.of(new OrderCriteria.Item(1L, 1L, 4))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 두 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.order(new OrderCriteria.Order(2L, List.of(new OrderCriteria.Item(1L, 1L, 3))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 세 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.order(new OrderCriteria.Order(3L, List.of(new OrderCriteria.Item(1L, 1L, 3))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            countDownLatch.await();

            // assert
            long successfulOrders = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .count();

            Optional<Inventory> savedInv = inventoryJpaRepository.findById(1L);
            Optional<User> savedUser1 = userJpaRepository.findById(1L);
            Optional<User> savedUser2 = userJpaRepository.findById(2L);
            assertThat(savedInv.isPresent()).isTrue();
            assertThat(savedUser1.isPresent()).isTrue();
            assertThat(savedUser2.isPresent()).isTrue();
            assertAll(
                () -> assertThat(successfulOrders).isEqualTo(3),
                () -> assertThat(savedInv.get().getQuantity()).isEqualTo(0)
            );
        }
    }
}
