package com.loopers.interfaces.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.like.LikeV1Dto;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LikeV1ApiE2ETest {

    private static final Function<Long, String> ENDPOINT = productId -> "/api/v1/like/products/" + productId;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/like/products/{productId}")
    @Nested
    class POST {
        @DisplayName("올바른 상품 ID, 유저 ID가 주어지면 상품을 좋아요 할 수 있다.")
        @Test
        void likeProduct() {
            // arrange
            productJpaRepository.save(new Product(
                "테스트 상품",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1)));

            userJpaRepository.save(new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@mail.com"));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<LikeV1Dto.LikeResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<LikeV1Dto.LikeResponse>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.POST, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertTrue(likeJpaRepository.existsByTarget(new LikeTarget(1L, LikeTargetType.PRODUCT))),
                () -> assertThat(response.getBody().data().userId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().targetId()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().targetType()).isEqualTo("PRODUCT")
            );
        }

        @DisplayName("존재하지 않는 상품 ID가 주어지면, 상품 좋아요에 실패한다.")
        @Test
        void likeProduct_whenInvalidProductId() {
            // arrange
            userJpaRepository.save(new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@mail.com"));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.POST, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("상품을 찾을 수 없습니다. 상품 ID: 1"));
        }

        @DisplayName("존재하지 않는 유저 ID가 주어지면, 상품 좋아요에 실패한다.")
        @Test
        void likeProduct_whenInvalidUserId() {
            // arrange
            productJpaRepository.save(new Product(
                "테스트 상품",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1)));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.POST, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("해당 ID의 유저가 존재하지 않습니다. [userId = testUser]"));
        }
    }

    @DisplayName("DELETE /api/v1/like/products/{productId}")
    @Nested
    class DELETE {
        @DisplayName("올바른 상품 ID, 유저 ID가 주어지면 상품 좋아요를 취소할 수 있다.")
        @Test
        void cancelLikeProduct() {
            // arrange
            productJpaRepository.save(new Product(
                "테스트 상품",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1)));

            userJpaRepository.save(new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@mail.com"));

            likeJpaRepository.save(new Like(
                1L,
                1L,
                LikeTargetType.PRODUCT));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(likeJpaRepository.existsByTarget(new LikeTarget(1L, LikeTargetType.PRODUCT))).isFalse());
        }

        @DisplayName("존재하지 않는 상품 ID가 주어지면, 상품 좋아요 취소에 실패한다.")
        @Test
        void cancelLikeProduct_whenInvalidProductId() {
            // arrange
            userJpaRepository.save(new User(
                "testUser",
                Gender.MALE,
                "1997-06-05",
                "test@mail.com"));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("상품을 찾을 수 없습니다. 상품 ID: 1"));
        }

        @DisplayName("존재하지 않는 유저 ID가 주어지면, 상품 좋아요 취소에 실패한다.")
        @Test
        void cancelLikeProduct_whenInvalidUserId() {
            // arrange
            productJpaRepository.save(new Product(
                "테스트 상품",
                null,
                BigDecimal.valueOf(10000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(1)));

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", "testUser");
            String requestUrl = ENDPOINT.apply(1L);

            // act
            ParameterizedTypeReference<ApiResponse<Void>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(requestUrl,
                HttpMethod.DELETE, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getBody().meta().message()).isEqualTo("해당 ID의 유저가 존재하지 않습니다. [userId = testUser]"));
        }
    }
}
