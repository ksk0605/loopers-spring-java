package com.loopers.interfaces.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
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
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.interfaces.api.product.ProductV1Dto;
import com.loopers.utils.DatabaseCleanUp;

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
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/products/{productId}")
    @Nested
    class Get {
        @DisplayName("존재하는 상품 ID를 주면, 해당 상품 정보를 반환한다.")
        @Test
        void returnsProductInfos_whenValidProductIdProvided() {
            // arrange
            productJpaRepository.save(new Product(
                "테스트 상품",
                null,
                BigDecimal.valueOf(20000),
                ProductStatus.ON_SALE,
                1L,
                1L
            ));
            brandJpaRepository.save(new Brand(
                "테스트 브랜드",
                "테스트 브랜드 설명",
                null
            ));
            String requestUrl = ENDPOINT.apply("/" + 1);

            // act
            ParameterizedTypeReference<ApiResponse<ProductV1Dto.ProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<ProductV1Dto.ProductResponse>> response =
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
}
