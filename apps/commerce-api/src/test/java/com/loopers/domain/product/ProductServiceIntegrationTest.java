package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.like.LikeSummaryJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private LikeSummaryJpaRepository likeSummaryJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @BeforeEach
    void setUp() {
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

        LikeSummary summary1 = new LikeSummary(1L, LikeTargetType.PRODUCT);
        LikeSummary summary2 = new LikeSummary(2L, LikeTargetType.PRODUCT);
        LikeSummary summary3 = new LikeSummary(3L, LikeTargetType.PRODUCT);
        summary1.incrementLikeCount();
        summary2.incrementLikeCount();
        summary2.incrementLikeCount();
        summary2.incrementLikeCount();
        summary3.incrementLikeCount();
        summary3.incrementLikeCount();
        likeSummaryJpaRepository.save(summary1);
        likeSummaryJpaRepository.save(summary2);
        likeSummaryJpaRepository.save(summary3);

    }

    @DisplayName("상품 목록을 조회할 때, ")
    @Nested
    class GetAll {
        @DisplayName("검색 조건을 가격 오름차순으로 가져올 수 있다.")
        @Test
        void getProducts_orderBygetPrice() {
            // arrange
            var command = new ProductCommand.Search(
                SortBy.PRICE_ASC,
                0,
                10,
                ProductStatus.ON_SALE
            );

            // act
            var products = productService.getAll(command);

            // assert
            assertAll(
                () -> assertThat(products.getContent()).hasSize(3),
                // 2 -> 1 -> 3번 순서
                () -> assertThat(products.getContent().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000)),
                () -> assertThat(products.getContent().get(0).getId()).isEqualByComparingTo(2L),
                () -> assertThat(products.getContent().get(1).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000)),
                () -> assertThat(products.getContent().get(1).getId()).isEqualByComparingTo(1L),
                () -> assertThat(products.getContent().get(2).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000)),
                () -> assertThat(products.getContent().get(2).getId()).isEqualByComparingTo(3L)
            );
        }

        @DisplayName("검색 조건을 좋아요 내림차순으로 가져올 수 있다.")
        @Test
        void getProducts_orderByLikes() {
            // arrange
            var command = new ProductCommand.Search(
                SortBy.LIKES_DESC,
                0,
                10,
                ProductStatus.ON_SALE
            );

            // act
            var products = productService.getAll(command);

            // assert
            assertAll(
                () -> assertThat(products.getContent()).hasSize(3),
                // 2 -> 3 -> 1번 순서
                () -> assertThat(products.getContent().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000)),
                () -> assertThat(products.getContent().get(0).getId()).isEqualByComparingTo(2L),
                () -> assertThat(products.getContent().get(1).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000)),
                () -> assertThat(products.getContent().get(1).getId()).isEqualByComparingTo(3L),
                () -> assertThat(products.getContent().get(2).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000)),
                () -> assertThat(products.getContent().get(2).getId()).isEqualByComparingTo(1L)
            );
        }

        @DisplayName("검색 조건을 최신순으로 가져올 수 있다.")
        @Test
        void getProducts_orderByLatest() {
            // arrange
            var command = new ProductCommand.Search(
                SortBy.LATEST,
                0,
                10,
                ProductStatus.ON_SALE
            );

            // act
            var products = productService.getAll(command);

            // assert
            assertAll(
                () -> assertThat(products.getContent()).hasSize(3),
                // 3 -> 2 -> 1번 순서
                () -> assertThat(products.getContent().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(30000)),
                () -> assertThat(products.getContent().get(0).getId()).isEqualByComparingTo(3L),
                () -> assertThat(products.getContent().get(1).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000)),
                () -> assertThat(products.getContent().get(1).getId()).isEqualByComparingTo(2L),
                () -> assertThat(products.getContent().get(2).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20000)),
                () -> assertThat(products.getContent().get(2).getId()).isEqualByComparingTo(1L)
            );
        }
    }
}
