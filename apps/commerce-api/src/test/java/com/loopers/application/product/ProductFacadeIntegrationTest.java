package com.loopers.application.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class ProductFacadeIntegrationTest {

    @Autowired
    private ProductFacade productService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private ProductFacade productFacade;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 정보를 조회할 때, ")
    @Nested
    class Get {
        @DisplayName("올바른 상품 ID가 주어지면, 해당하는 상품 정보를 반환한다.")
        @Test
        void returnsProductInfo_whenValidProductIdIsProvided() {
            // arrange
            productJpaRepository.save(
                new Product(
                    "상품 이름",
                    "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.",
                    BigDecimal.valueOf(20000),
                    ProductStatus.ON_SALE,
                    1L,
                    1L,
                    LocalDateTime.now().plusDays(3)
                )
            );
            brandJpaRepository.save(
                new Brand(
                    "테스트 브랜드",
                    null,
                    null
                )
            );
            likeJpaRepository.save(
                new Like(
                    1L,
                    1L,
                    LikeTargetType.PRODUCT
                )
            );

            // act
            ProductInfo productInfo = productFacade.getProduct(1L);

            // assert
            assertAll(
                () -> assertThat(productInfo.id()).isEqualTo(1L),
                () -> assertThat(productInfo.brand().name()).isEqualTo("테스트 브랜드"),
                () -> assertThat(productInfo.brand().id()).isEqualTo(1L),
                () -> assertThat(productInfo.likeCount()).isEqualTo(1L)
            );

        }
    }
}
