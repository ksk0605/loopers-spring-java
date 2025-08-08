package com.loopers.application.product;

import static com.loopers.support.fixture.BrandFixture.aBrand;
import static com.loopers.support.fixture.LikeFixture.aLike;
import static com.loopers.support.fixture.ProductFixture.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.IntegrationTest;

@SpringBootTest
class ProductFacadeIntegrationTest extends IntegrationTest {
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private ProductFacade productFacade;

    @DisplayName("상품 정보를 조회할 때, ")
    @Nested
    class Get {
        @DisplayName("올바른 상품 ID가 주어지면, 해당하는 상품 정보를 반환한다.")
        @Test
        void returnsProductInfo_whenValidProductIdIsProvided() {
            // arrange
            productJpaRepository.save(aProduct().build());
            brandJpaRepository.save(aBrand().build());
            likeJpaRepository.save(aLike().build());

            // act
            ProductResult productResult = productFacade.getProduct(1L);

            // assert
            assertAll(
                () -> assertThat(productResult.id()).isEqualTo(1L),
                () -> assertThat(productResult.brand().name()).isEqualTo("테스트 브랜드"),
                () -> assertThat(productResult.brand().id()).isEqualTo(1L),
                () -> assertThat(productResult.likeCount()).isEqualTo(1L)
            );

        }
    }
}
