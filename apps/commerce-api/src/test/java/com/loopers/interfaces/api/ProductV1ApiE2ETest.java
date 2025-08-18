package com.loopers.interfaces.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.interfaces.api.product.ProductV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import com.loopers.utils.RedisCleanUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductV1ApiE2ETest {
    private static Function<String, String> ENDPOINT = source -> "/api/v1/products" + source;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private RedisCleanUp redisCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
        redisCleanUp.truncateAll();
    }

    @DisplayName("GET /api/v1/products/{productId}")
    @Nested
    class GetProduct {
        @DisplayName("존재하는 상품 ID를 주면, 해당 상품 정보를 반환한다.")
        @Test
        void returnsProductInfo_whenValidProductIdProvided() {
            // arrange
            productJpaRepository.save(new Product(
                "테스트 상품",
                null,
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L,
                LocalDateTime.now().plusDays(3)
            ));
            brandJpaRepository.save(new Brand(
                "테스트 브랜드",
                "테스트 브랜드 설명",
                null
            ));
            String requestUrl = ENDPOINT.apply("/" + 1);

            // act
            ParameterizedTypeReference<ApiResponse<ProductV1Dto.ProductDetailResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<ProductV1Dto.ProductDetailResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().id()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().name()).isEqualTo("테스트 상품"),
                () -> assertThat(response.getBody().data().brand().id()).isEqualTo(1L),
                () -> assertThat(response.getBody().data().brand().name()).isEqualTo("테스트 브랜드"),
                () -> assertThat(response.getBody().data().brand().logoUrl()).isNull(),
                () -> assertThat(response.getBody().data().likeCount()).isEqualTo(0)
            );
        }
    }

    @DisplayName("GET /api/v1/products")
    @Nested
    class GetProducts {
        @DisplayName("판매중 상태인 상품 정보 목록을 반환한다.")
        @Test
        void returnsProductInfos_onSale() {
            // arrange
            brandJpaRepository.save(new Brand("브랜드 명", null, null));
            productJpaRepository.save(
                new Product(
                    "상품 1",
                    null,
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L,
                    LocalDateTime.now().plusDays(1)
                )
            );
            productJpaRepository.save(
                new Product(
                    "상품 2",
                    null,
                    BigDecimal.valueOf(10000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L,
                    LocalDateTime.now().plusDays(3)
                )
            );
            productJpaRepository.save(
                new Product(
                    "상품 3",
                    null,
                    BigDecimal.valueOf(30000),
                    ProductStatus.DISCONTINUED,
                    1L,
                    1L,
                    LocalDateTime.now().plusDays(5)
                )
            );

            // 좋아요 세팅
            likeJpaRepository.save(new Like(1L, 1L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(1L, 2L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(1L, 3L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(2L, 2L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(2L, 3L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(3L, 2L, LikeTargetType.PRODUCT));

            String requestUrl = ENDPOINT.apply("?page=0&size=10&sort=price_asc");

            // act
            ParameterizedTypeReference<ApiResponse<ProductV1Dto.ProductsResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<ProductV1Dto.ProductsResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().products()).hasSize(2),
                () -> assertThat(response.getBody().data().pageInfo().currentPage()).isEqualTo(0),
                () -> assertThat(response.getBody().data().pageInfo().hasNext()).isFalse(),
                () -> assertThat(response.getBody().data().pageInfo().totalPages()).isEqualTo(1)
            );
        }
    }
}
