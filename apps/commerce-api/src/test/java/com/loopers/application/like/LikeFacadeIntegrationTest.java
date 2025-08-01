package com.loopers.application.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.application.product.ProductResult;
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
class LikeFacadeIntegrationTest {
    @Autowired
    private LikeFacade likeFacade;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("좋아요를 추가 할 때, ")
    @Nested
    class GetLikedProducts {
        @DisplayName("내가 좋아요한 상품 목록을 조회할 때, 해당 타입의 좋아요 목록을 반환한다.")
        @Test
        void returnsProductResult_whenTargetTypeProvided() {
            // arrange
            brandJpaRepository.save(new Brand("브랜드1", null, null));

            productJpaRepository.save(new Product("상품1", null, BigDecimal.valueOf(10000), ProductStatus.ON_SALE, 1L, 1L, LocalDateTime.now().plusDays(1)));
            productJpaRepository.save(new Product("상품2", null, BigDecimal.valueOf(10000), ProductStatus.ON_SALE, 1L, 1L, LocalDateTime.now().plusDays(1)));
            productJpaRepository.save(new Product("상품3", null, BigDecimal.valueOf(10000), ProductStatus.ON_SALE, 1L, 1L, LocalDateTime.now().plusDays(1)));
            productJpaRepository.save(new Product("상품4", null, BigDecimal.valueOf(10000), ProductStatus.ON_SALE, 1L, 1L, LocalDateTime.now().plusDays(1)));

            likeJpaRepository.save(new Like(1L, 1L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(1L, 2L, LikeTargetType.BRAND));
            likeJpaRepository.save(new Like(1L, 3L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(1L, 4L, LikeTargetType.PRODUCT));

            // act
            List<ProductResult> results = likeFacade.getLikedProducts(1L, LikeTargetType.PRODUCT);

            // assert
            assertAll(
                () -> assertThat(results).hasSize(3),
                () -> assertThat(results.get(0).id()).isEqualTo(1L),
                () -> assertThat(results.get(1).id()).isEqualTo(3L),
                () -> assertThat(results.get(2).id()).isEqualTo(4L)
            );
        }
    }
}
