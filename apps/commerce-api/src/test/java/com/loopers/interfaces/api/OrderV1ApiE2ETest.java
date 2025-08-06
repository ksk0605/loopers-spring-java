package com.loopers.interfaces.api;

import static com.loopers.support.fixture.OrderFixture.anOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.loopers.domain.inventory.Inventory;
import com.loopers.domain.payment.Payment;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductOption;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.inventory.InventoryJpaRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.infrastructure.payment.PaymentJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.order.OrderV1Dto;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderV1ApiE2ETest {
    private static final String ENDPOINT = "/api/v1/orders";
    private static final Function<Long, String> GET_ORDER_ENDPOINT = orderId -> "/api/v1/orders/" + orderId;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private InventoryJpaRepository inventoryJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/orders")
    @Nested
    class CreateOrder {
        @DisplayName("올바른 주문 생성 요청을 주면, 주문 생성 성공 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenValidRequestIsProvided() {
            // arrange
            User user = new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@loopers.com");
            user.chargePoint(30000);
            userJpaRepository.save(
                user);

            Product product = new Product(
                "상품 이름",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1));
            product.addOption(new ProductOption(
                "SIZE",
                "M",
                BigDecimal.valueOf(1000)));
            product.addOption(new ProductOption(
                "SIZE",
                "L",
                BigDecimal.valueOf(2000)));
            productJpaRepository.save(product);

            inventoryJpaRepository.save(new Inventory(1L, 2L, 10));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");

            OrderV1Dto.OrderRequest request = new OrderV1Dto.OrderRequest(
                List.of(new OrderV1Dto.OrderItemRequest(1L, 2L, 1)));

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponse>> response = testRestTemplate.exchange(ENDPOINT,
                HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().id()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().items()).hasSize(1),
                () -> assertThat(response.getBody().data().items().get(0).productId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().items().get(0).productOptionId()).isEqualTo(2L),
                () -> assertThat(response.getBody().data().items().get(0).quantity()).isEqualTo(1),
                () -> assertThat(response.getBody().data().userId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().orderStatus()).isEqualTo("PAYMENT_COMPLETED"),
                () -> assertThat(response.getBody().data().paymentMethod()).isEqualTo("POINT"),
                () -> assertThat(response.getBody().data().paymentStatus()).isEqualTo("COMPLETED"),
                () -> assertThat(response.getBody().data().orderDate()).isAfter(LocalDateTime.now().minusDays(1)),
                () -> assertThat(response.getBody().data().totalPrice()).isEqualTo(12000L));
        }

        @DisplayName("재고가 부족할 경우, 주문 생성 실패 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenInsufficientInventory() {
            // arrange
            User user = new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@loopers.com");
            user.chargePoint(30000);
            userJpaRepository.save(
                user);

            Product product = new Product(
                "상품 이름",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1));
            product.addOption(new ProductOption(
                "SIZE",
                "M",
                BigDecimal.valueOf(1000)));
            product.addOption(new ProductOption(
                "SIZE",
                "L",
                BigDecimal.valueOf(2000)));
            productJpaRepository.save(product);

            inventoryJpaRepository.save(new Inventory(1L, 1L, 5));
            inventoryJpaRepository.save(new Inventory(1L, 2L, 10));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");

            OrderV1Dto.OrderRequest request = new OrderV1Dto.OrderRequest(
                List.of(
                    new OrderV1Dto.OrderItemRequest(1L, 1L, 6),
                    new OrderV1Dto.OrderItemRequest(1L, 2L, 5)));

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponse>> response = testRestTemplate.exchange(ENDPOINT,
                HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("재고가 부족합니다. 요청: 6, 재고: 5"));
        }

        @DisplayName("존재하지 않는 상품을 주문할 경우, 주문 생성 실패 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenInvalidProduct() {
            // arrange
            User user = new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@loopers.com");
            user.chargePoint(30000);
            userJpaRepository.save(user);

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");

            OrderV1Dto.OrderRequest request = new OrderV1Dto.OrderRequest(
                List.of(new OrderV1Dto.OrderItemRequest(1L, 2L, 1)));

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponse>> response = testRestTemplate.exchange(ENDPOINT,
                HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("상품을 찾을 수 없습니다. 상품 ID: 1"));
        }

        @DisplayName("포인트가 부족할 경우, 주문 생성 실패 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenInvalidProductOption() {
            // arrange
            User user = new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@loopers.com");
            user.chargePoint(22999);
            userJpaRepository.save(user);

            Product product = new Product(
                "상품 이름",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1));
            product.addOption(new ProductOption(
                "SIZE",
                "M",
                BigDecimal.valueOf(1000)));
            product.addOption(new ProductOption(
                "SIZE",
                "L",
                BigDecimal.valueOf(2000)));
            productJpaRepository.save(product);

            inventoryJpaRepository.save(new Inventory(1L, 1L, 10));
            inventoryJpaRepository.save(new Inventory(1L, 2L, 10));
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");

            OrderV1Dto.OrderRequest request = new OrderV1Dto.OrderRequest(
                List.of(
                    new OrderV1Dto.OrderItemRequest(1L, 1L, 1),
                    new OrderV1Dto.OrderItemRequest(1L, 2L, 1)));

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponse>> response = testRestTemplate.exchange(ENDPOINT,
                HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("포인트가 부족합니다."));
        }
    }

    @DisplayName("GET /api/v1/orders")
    @Nested
    class GetOrders {
        @DisplayName("올바른 유저의 ID를 주면, 주문 목록 조회 성공 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenValidUserId() {
            // arrange
            userJpaRepository.save(new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@loopers.com"));

            Product product = new Product(
                "상품 이름",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1));
            product.addOption(new ProductOption(
                "SIZE",
                "M",
                BigDecimal.valueOf(1000)));
            product.addOption(new ProductOption(
                "SIZE",
                "L",
                BigDecimal.valueOf(2000)));
            productJpaRepository.save(product);

            orderJpaRepository.save(anOrder().build());
            paymentJpaRepository
                .save(new Payment(1L, PaymentMethod.POINT, PaymentStatus.COMPLETED, BigDecimal.valueOf(10000)));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponses>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponses>> response = testRestTemplate.exchange(ENDPOINT,
                HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().orders()).hasSize(1),
                () -> assertThat(response.getBody().data().orders().get(0).id()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().orders().get(0).items()).hasSize(1),
                () -> assertThat(response.getBody().data().orders().get(0).items().get(0).productId())
                    .isEqualTo(1L),
                () -> assertThat(response.getBody().data().orders().get(0).items().get(0).productOptionId())
                    .isEqualTo(1L),
                () -> assertThat(response.getBody().data().orders().get(0).items().get(0).quantity()).isEqualTo(1),
                () -> assertThat(response.getBody().data().orders().get(0).userId()).isEqualTo(1L));
        }

        @DisplayName("존재하지 않는 유저의 ID를 주면, 주문 목록 조회 실패 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenInvalidUserId() {
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "invalidUser");

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponses>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponses>> response = testRestTemplate.exchange(ENDPOINT,
                HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()));
        }
    }

    @DisplayName("GET /api/v1/orders/{orderId}")
    @Nested
    class GetOrder {
        @DisplayName("올바른 주문 ID를 주면, 주문 조회 성공 응답을 반환한다.")
        @Test
        void returnsOrderInfo_whenValidOrderId() {
            // arrange
            userJpaRepository.save(new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@loopers.com"));

            Product product = new Product(
                "상품 이름",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1));
            product.addOption(new ProductOption(
                "SIZE",
                "M",
                BigDecimal.valueOf(1000)));
            product.addOption(new ProductOption(
                "SIZE",
                "L",
                BigDecimal.valueOf(2000)));
            productJpaRepository.save(product);

            orderJpaRepository.save(anOrder().build());
            paymentJpaRepository
                .save(new Payment(1L, PaymentMethod.POINT, PaymentStatus.COMPLETED, BigDecimal.valueOf(22000)));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = GET_ORDER_ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<OrderV1Dto.OrderResponse>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().id()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().items()).hasSize(1),
                () -> assertThat(response.getBody().data().items().get(0).productId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().items().get(0).productOptionId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().items().get(0).quantity()).isEqualTo(1),
                () -> assertThat(response.getBody().data().userId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().orderStatus()).isEqualTo("PENDING"),
                () -> assertThat(response.getBody().data().paymentMethod()).isEqualTo("POINT"),
                () -> assertThat(response.getBody().data().paymentStatus()).isEqualTo("COMPLETED"),
                () -> assertThat(response.getBody().data().totalPrice()).isEqualTo(22000L));
        }
    }
}
