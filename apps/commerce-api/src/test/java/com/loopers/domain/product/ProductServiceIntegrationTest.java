package com.loopers.domain.product;

import static com.loopers.support.fixture.LikeFixture.aLike;
import static com.loopers.support.fixture.ProductFixture.aProduct;
import static com.loopers.support.fixture.UserSignalFixture.anUserSignal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.usersignal.UserSignal;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.usersignal.UserSignalJpaRepository;
import com.loopers.support.IntegrationTest;

public class ProductServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    @MockitoSpyBean
    private ProductCacheRepository productCacheRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    @MockitoSpyBean
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private UserSignalJpaRepository userSignalJpaRepository;

    @DisplayName("상품 목록을 조회할 때, ")
    @Nested
    class GetAll {
        @BeforeEach
        void setUp() {
            brandJpaRepository.save(new Brand("브랜드 명", null, null));

            // 상품 목록 세팅
            Product product = aProduct().name("상품 1").price(BigDecimal.valueOf(20000)).saleStartDate(LocalDateTime.now().plusDays(1)).build();
            product.addImage("testUrl", true);
            productJpaRepository.save(product);
            productJpaRepository.save(aProduct().name("상품 2").price(BigDecimal.valueOf(10000)).saleStartDate(LocalDateTime.now().plusDays(3)).build());
            productJpaRepository.save(aProduct().name("상품 3").price(BigDecimal.valueOf(30000)).saleStartDate(LocalDateTime.now().plusDays(5)).build());

            // 좋아요 세팅
            likeJpaRepository.save(aLike().targetId(1L).build());
            likeJpaRepository.save(aLike().targetId(2L).build());
            likeJpaRepository.save(aLike().targetId(3L).build());
            likeJpaRepository.save(aLike().targetId(1L).build());
            likeJpaRepository.save(aLike().userId(2L).targetId(2L).build());
            likeJpaRepository.save(aLike().userId(2L).targetId(3L).build());
            likeJpaRepository.save(aLike().userId(3L).targetId(2L).build());

            UserSignal signal1 = anUserSignal().build();
            UserSignal signal2 = anUserSignal().targetId(2L).build();
            UserSignal signal3 = anUserSignal().targetId(3L).build();
            signal1.updateLikeCount(1L);
            signal2.updateLikeCount(3L);
            signal3.updateLikeCount(2L);
            userSignalJpaRepository.save(signal1);
            userSignalJpaRepository.save(signal2);
            userSignalJpaRepository.save(signal3);

        }

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

    @DisplayName("상품 상세 정보를 조회할 때, ")
    @Nested
    class GetInfoWithCache {
        @DisplayName("캐시에 상품 상세 정보가 있으면 캐시에서 반환한다.")
        @Test
        void returnsProductInfo_fromCache() {
            // arrange
            Product product = aProduct().build();
            productCacheRepository.setProductInfo(1L, ProductInfo.from(product));

            // act
            var productInfo = productService.getInfo(1L);

            // assert
            verify(productCacheRepository, times(1)).getProductInfo(1L);
            assertThat(productInfo.getName()).isEqualTo(product.getName());
            assertThat(productInfo.getPrice()).isEqualTo(product.getPrice().longValue());
            assertThat(productInfo.getStatus()).isEqualTo(product.getStatus().name());
            assertThat(productInfo.getBrandId()).isEqualTo(product.getBrandId());
            assertThat(productInfo.getImageUrls()).hasSize(product.getImages().size());
            assertThat(productInfo.getOptions()).hasSize(product.getOptions().size());
        }

        @DisplayName("캐시에 상품 상세 정보가 없으면 캐시를 저장하고, 데이터베이스에서 조회한다.")
        @Test
        void returnsProductInfo_fromDatabase() {
            // arrange
            Product product = aProduct().build();
            productJpaRepository.save(product);

            // act
            var productInfo = productService.getInfo(1L);

            // assert
            verify(productCacheRepository, times(1)).setProductInfo(any(Long.class), any(ProductInfo.class));
            verify(productJpaRepository, times(1)).findById(any(Long.class));
            assertThat(productInfo.getName()).isEqualTo(product.getName());
            assertThat(productInfo.getPrice()).isEqualTo(product.getPrice().longValue());
            assertThat(productInfo.getStatus()).isEqualTo(product.getStatus().name());
            assertThat(productInfo.getBrandId()).isEqualTo(product.getBrandId());
            assertThat(productInfo.getImageUrls()).hasSize(product.getImages().size());
            assertThat(productInfo.getOptions()).hasSize(product.getOptions().size());
        }
    }
}
