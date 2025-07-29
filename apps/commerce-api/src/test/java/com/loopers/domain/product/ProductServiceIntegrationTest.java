package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        // 브랜드 세팅
        brandJpaRepository.save(new Brand("브랜드 명", null, null));

        // 상품 목록 세팅
        Product product = new Product(
            "상품 1",
            null,
            BigDecimal.valueOf(20000),
            ProductStatus.ON_SALE,
            1L,
            1L,
            LocalDateTime.now().plusDays(1)
        );
        product.addImage("testUrl", true);
        productJpaRepository.save(
            product
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
                ProductStatus.ON_SALE,
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
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 목록을 조회할 때, ")
    @Nested
    class GetProducts {
        @DisplayName("검색 조건을 가격 순으로 가져올 수 있다.")
        @Test
        void getProducts_orderByPrice() {
            // arrange
            ProductSearchCondition condition = new ProductSearchCondition(
                SortBy.PRICE,
                0,
                20,
                ProductStatus.ON_SALE
            );

            // act
            Page<ProductView> products = productService.list(condition);

            // assert
            assertThat(products.getContent()).hasSize(3);
            // 2 -> 1 -> 3번 순서
            assertThat(products.getContent().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
            assertThat(products.getContent().get(0).getId()).isEqualByComparingTo(2L);
            assertThat(products.getContent().get(1).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000));
            assertThat(products.getContent().get(1).getId()).isEqualByComparingTo(1L);
            assertThat(products.getContent().get(2).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000));
            assertThat(products.getContent().get(2).getId()).isEqualByComparingTo(3L);
        }

        @DisplayName("검색 조건을 좋아요 순으로 가져올 수 있다.")
        @Test
        void getProducts_orderByLikes() {
            // arrange
            ProductSearchCondition condition = new ProductSearchCondition(
                SortBy.LIKES,
                0,
                20,
                ProductStatus.ON_SALE
            );

            // act
            Page<ProductView> products = productService.list(condition);

            // assert
            // 2 -> 3 -> 1번 순서
            assertThat(products.getContent()).hasSize(3);
            assertThat(products.getContent().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
            assertThat(products.getContent().get(0).getId()).isEqualByComparingTo(2L);
            assertThat(products.getContent().get(1).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000));
            assertThat(products.getContent().get(1).getId()).isEqualByComparingTo(3L);
            assertThat(products.getContent().get(2).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000));
            assertThat(products.getContent().get(2).getId()).isEqualByComparingTo(1L);
        }

        @DisplayName("검색 조건을 최신순으로 가져올 수 있다.")
        @Test
        void getProducts_orderByLatest() {
            // arrange
            ProductSearchCondition condition = new ProductSearchCondition(
                SortBy.LATEST,
                0,
                20,
                ProductStatus.ON_SALE
            );

            // act
            Page<ProductView> products = productService.list(condition);

            // assert
            assertThat(products.getContent()).hasSize(3);
            assertThat(products.getContent().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000));
            assertThat(products.getContent().get(0).getId()).isEqualByComparingTo(3L);
            assertThat(products.getContent().get(1).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
            assertThat(products.getContent().get(1).getId()).isEqualByComparingTo(2L);
            assertThat(products.getContent().get(2).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000));
            assertThat(products.getContent().get(2).getId()).isEqualByComparingTo(1L);
        }
    }
}
