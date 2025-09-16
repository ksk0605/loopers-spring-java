package com.loopers.application.product;

import static com.loopers.support.fixture.BrandFixture.aBrand;
import static com.loopers.support.fixture.LikeFixture.aLike;
import static com.loopers.support.fixture.ProductFixture.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.product.SortBy;
import com.loopers.domain.rank.RankCommand;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.IntegrationTest;

class ProductFacadeIntegrationTest extends IntegrationTest {

    @Autowired
    @MockitoSpyBean
    private ProductCacheRepository productCacheRepository;

    @Autowired
    @MockitoSpyBean
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private ProductFacade productFacade;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
            ProductDetailResult productResult = productFacade.getProduct(1L);

            // assert
            assertAll(
                () -> assertThat(productResult.id()).isEqualTo(1L),
                () -> assertThat(productResult.brand().name()).isEqualTo("테스트 브랜드"),
                () -> assertThat(productResult.brand().id()).isEqualTo(1L),
                () -> assertThat(productResult.likeCount()).isEqualTo(1L),
                () -> assertThat(productResult.options().size()).isEqualTo(0)
            );
        }
    }

    @DisplayName("상품 목록을 조회할 때, ")
    @Nested
    class GetProductsWithCache {
        @DisplayName("캐시에 상품 목록이 있으면, 캐시에서 반환한다.")
        @Test
        void returnsProductResults_fromCache() {
            // arrange
            productJpaRepository.save(aProduct().build());
            productJpaRepository.save(aProduct().build());
            productJpaRepository.save(aProduct().build());
            brandJpaRepository.save(aBrand().build());
            likeJpaRepository.save(aLike().build());
            likeJpaRepository.save(aLike().build());
            likeJpaRepository.save(aLike().build());

            ProductCommand.Search command = new ProductCommand.Search(SortBy.LATEST, 0, 10, ProductStatus.ON_SALE);
            productFacade.getProducts(command);

            // act
            ProductResults result = productFacade.getProducts(command);

            // assert
            verify(productCacheRepository, times(1)).setProductResults(any(ProductCommand.Search.class), any(ProductResults.class));
            verify(productCacheRepository, times(2)).getProductResults(any(ProductCommand.Search.class));
            assertThat(result.products()).hasSize(3);
            assertThat(result.pageInfo().totalElements()).isEqualTo(3);
            assertThat(result.pageInfo().totalPages()).isEqualTo(1);
            assertThat(result.pageInfo().currentPage()).isEqualTo(0);
            assertThat(result.pageInfo().pageSize()).isEqualTo(10);
            assertThat(result.pageInfo().hasNext()).isFalse();
        }

        @DisplayName("캐시에 상품 목록이 없으면, 데이터베이스에서 조회하고 캐시에 저장한다.")
        @Test
        void returnsProductResults_fromDatabase() {
            // arrange
            productJpaRepository.save(aProduct().build());
            productJpaRepository.save(aProduct().build());
            productJpaRepository.save(aProduct().build());
            brandJpaRepository.save(aBrand().build());
            likeJpaRepository.save(aLike().build());
            likeJpaRepository.save(aLike().build());

            // act
            ProductResults result = productFacade.getProducts(new ProductCommand.Search(SortBy.LATEST, 0, 10, ProductStatus.ON_SALE));

            // assert
            verify(productCacheRepository, times(1)).setProductResults(any(ProductCommand.Search.class), any(ProductResults.class));
            verify(productJpaRepository, times(1)).findByStatusOrderByLatest(any(ProductStatus.class), any(Pageable.class));
            assertThat(result.products()).hasSize(3);
            assertThat(result.pageInfo().totalElements()).isEqualTo(3);
            assertThat(result.pageInfo().totalPages()).isEqualTo(1);
            assertThat(result.pageInfo().currentPage()).isEqualTo(0);
            assertThat(result.pageInfo().pageSize()).isEqualTo(10);
            assertThat(result.pageInfo().hasNext()).isFalse();
        }
    }

    @DisplayName("데일리 랭킹 순 상품을 조회할 때, ")
    @Nested
    class GetDailyRanking {
        @DisplayName("날짜, 페이지, 사이즈가 주어지면 상품 목록을 반환한다.")
        @Test
        void returnsProductResults() {
            // arrange
            productJpaRepository.save(aProduct().name("상품1").build());
            productJpaRepository.save(aProduct().name("상품2").build());
            productJpaRepository.save(aProduct().name("상품3").build());
            productJpaRepository.save(aProduct().name("상품4").build());
            productJpaRepository.save(aProduct().name("상품5").build());
            brandJpaRepository.save(aBrand().build());

            LocalDate today = LocalDate.now();
            String rankingKey = "rank:all:" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // 총 5개의 상품을 랭킹에 추가 (점수: 10.0 ~ 5.0)
            for (int i = 0; i < 5; i++) {
                redisTemplate.opsForZSet().add(rankingKey, String.valueOf(i + 1), (double)(10 - i));
            }

            // act
            ProductResults result = productFacade.getDailyRanking(new RankCommand.Get(today, 10, 1));

            // assert
            assertThat(result.products()).hasSize(5);
            assertThat(result.pageInfo().totalElements()).isEqualTo(5);
            assertThat(result.pageInfo().totalPages()).isEqualTo(1);
            assertThat(result.pageInfo().currentPage()).isEqualTo(1);
            assertThat(result.pageInfo().pageSize()).isEqualTo(5);
            assertThat(result.pageInfo().hasNext()).isFalse();
        }
    }
}
