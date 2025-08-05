package com.loopers.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductOption;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.inventory.InventoryJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트로 새로운 주문을 시도할 때, ")
    @Nested
    class OrderWithPoint {
        @DisplayName("유효한 주문 옵션 목록을 주면, 주문 결과를 반환한다. ")
        @Test
        void returnsOrderResults_whenValidOrderOptionsProvided() {
            // arrange
            User user = new User("testUser", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user.updatePoint(40000);
            userJpaRepository.save(user);

            Product product = new Product(
                "상품1",
                null,
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(3)
            );
            ProductOption productOption = new ProductOption("SIZE", "M", BigDecimal.ZERO);
            product.addOption(productOption);
            int initialStock = 5;
            Inventory inventory = new Inventory(1L, 1L, initialStock);
            productJpaRepository.save(product);
            inventoryJpaRepository.save(inventory);

            OrderCommand.Order command = new OrderCommand.Order(1L, List.of(new OrderCommand.OrderOption(1L, 1L, 2)));

            // act
            OrderResult result = orderFacade.placeOrder(command);

            // assert
            Optional<User> savedUser = userJpaRepository.findById(1L);
            assertThat(savedUser.isPresent()).isTrue();
            assertAll(
                () -> assertThat(result.userId()).isEqualTo(1L),
                () -> assertThat(result.totalPrice()).isEqualByComparingTo(BigDecimal.valueOf(40000)),
                () -> assertThat(savedUser.get().getPoint()).isEqualTo(0),
                () -> assertThat(result.paymentMethod()).isEqualTo(PaymentMethod.POINT),
                () -> assertThat(result.paymentStatus()).isEqualTo(PaymentStatus.COMPLETED),
                () -> assertThat(result.status()).isEqualTo(OrderStatus.PAYMENT_COMPLETED),
                () -> assertThat(result.items().get(0).productId()).isEqualTo(1L),
                () -> assertThat(result.items().get(0).productOptionId()).isEqualTo(1L),
                () -> assertThat(result.items().get(0).quantity()).isEqualTo(2)
            );
        }
    }

    @DisplayName("동일한 상품에 대해 동시에 주문을 요청할 때, ")
    @Nested
    class Concurrency {
        @DisplayName("두개의 주문 중 한 주문에 의해 재고가 부족해지는 경우, 하나만 성공하고 다른 주문은 실패한다")
        @Test
        void shouldPreventOverselling_whenConcurrentOrders() throws InterruptedException {
            // arrange
            User user1 = new User("testUser", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user1.updatePoint(1000000);
            userJpaRepository.save(user1);
            User user2 = new User("testUser", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user2.updatePoint(1000000);
            userJpaRepository.save(user2);

            Product product = new Product(
                "상품1",
                null,
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(3)
            );
            ProductOption productOption = new ProductOption("SIZE", "M", BigDecimal.ZERO);
            product.addOption(productOption);
            int initialStock = 5;
            Inventory inventory = new Inventory(1L, 1L, initialStock);
            productJpaRepository.save(product);
            inventoryJpaRepository.save(inventory);

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch countDownLatch = new CountDownLatch(2);

            // act
            List<Future<OrderResult>> futures = new ArrayList<>();

            // 첫 번째 주문 (4개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(1L, List.of(new OrderCommand.OrderOption(1L, 1L, 4))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 두 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(2L, List.of(new OrderCommand.OrderOption(1L, 1L, 3))));
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
                () -> assertThat(savedInv.get().getQuantity()).isEqualTo(2)
            );
        }

        @DisplayName("재고가 충분하다면, 모든 주문을 정상적으로 완료시킨다.")
        @Test
        void completeAllOrders_whenSufficientStock() throws InterruptedException {
            User user1 = new User("testUser1", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user1.updatePoint(6000000);
            userJpaRepository.save(user1);
            User user2 = new User("testUser2", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user2.updatePoint(6000000);
            userJpaRepository.save(user2);
            User user3 = new User("testUser3", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user3.updatePoint(6000000);
            userJpaRepository.save(user3);

            Product product = new Product(
                "상품1",
                null,
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(3)
            );
            ProductOption productOption = new ProductOption("SIZE", "M", BigDecimal.ZERO);
            product.addOption(productOption);
            int initialStock = 10;
            Inventory inventory = new Inventory(1L, 1L, initialStock);
            productJpaRepository.save(product);
            inventoryJpaRepository.save(inventory);

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch countDownLatch = new CountDownLatch(2);

            // act
            List<Future<OrderResult>> futures = new ArrayList<>();

            // 첫 번째 주문 (4개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(1L, List.of(new OrderCommand.OrderOption(1L, 1L, 4))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 두 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(2L, List.of(new OrderCommand.OrderOption(1L, 1L, 3))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 세 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(3L, List.of(new OrderCommand.OrderOption(1L, 1L, 3))));
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

    @DisplayName("동일한 유저가 여러 기기에서 동시에 주문할 때, ")
    @Nested
    class DuplicatedOrderFromSameUser {
        @DisplayName("포인트는 중복차감 되지 않고, 재고는 한번만 차감 처리된다.")
        @Test
        void preventDuplicateOrder_whenSameUserOrdered() throws InterruptedException {
            User user = new User("testUser1", Gender.MALE, "1997-06-05", "testUser@mail.com");
            user.updatePoint(6000000);
            userJpaRepository.save(user);

            Product product = new Product(
                "상품1",
                null,
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(3)
            );
            ProductOption productOption = new ProductOption("SIZE", "M", BigDecimal.ZERO);
            product.addOption(productOption);
            int initialStock = 10;
            Inventory inventory = new Inventory(1L, 1L, initialStock);
            productJpaRepository.save(product);
            inventoryJpaRepository.save(inventory);

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            CountDownLatch countDownLatch = new CountDownLatch(2);

            // act
            List<Future<OrderResult>> futures = new ArrayList<>();

            // 첫 번째 주문 (4개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(1L, List.of(new OrderCommand.OrderOption(1L, 1L, 4))));
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 두 번째 주문 (3개)
            futures.add(executorService.submit(() -> {
                try {
                    return orderFacade.placeOrder(new OrderCommand.Order(1L, List.of(new OrderCommand.OrderOption(1L, 1L, 4))));
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
            Optional<User> savedUser = userJpaRepository.findById(1L);
            assertThat(savedInv.isPresent()).isTrue();
            assertThat(savedUser.isPresent()).isTrue();
            assertAll(
                () -> assertThat(savedUser.get().getVersion()).isEqualTo(1L),
                () -> assertThat(successfulOrders).isEqualTo(1),
                () -> assertThat(savedInv.get().getQuantity()).isEqualTo(6)
            );
        }
    }
}
