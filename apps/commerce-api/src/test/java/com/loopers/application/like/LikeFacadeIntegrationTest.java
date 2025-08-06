package com.loopers.application.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.application.product.ProductResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.like.LikeSummaryJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
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
    private LikeSummaryJpaRepository likeSummaryJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

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
            userJpaRepository.save(new User("testUser", Gender.MALE, "1997-06-05", "loopers@loopers.com"));

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
            var criteria = new LikeCriteria.GetLiked("testUser", LikeTargetType.PRODUCT);
            List<ProductResult> results = likeFacade.getLikedProducts(criteria);

            // assert
            assertAll(
                () -> assertThat(results).hasSize(3),
                () -> assertThat(results.get(0).id()).isEqualTo(1L),
                () -> assertThat(results.get(1).id()).isEqualTo(3L),
                () -> assertThat(results.get(2).id()).isEqualTo(4L)
            );
        }
    }

    @DisplayName("동일 상품에 대해 여러명이 좋아요를 요청할 때, ")
    @Nested
    class ConcurrencyLike {
        @DisplayName("여러명이 좋아요를 한번에 요청하면 모든 사용자의 좋아요가 정상적으로 기록된다.")
        @Test
        void addLikeCount_whenConcurrencyLikeRequest() throws InterruptedException {
            // arrange
            brandJpaRepository.save(new Brand("브랜드1", null, null));
            productJpaRepository.save(new Product("상품1", null, BigDecimal.valueOf(10000), ProductStatus.ON_SALE, 1L, 1L, LocalDateTime.now().plusDays(1)));
            userJpaRepository.save(new User("testUser1", Gender.MALE, "1997-06-05", "loopers@loopers.com"));
            userJpaRepository.save(new User("testUser2", Gender.MALE, "1997-06-05", "loopers@loopers.com"));
            userJpaRepository.save(new User("testUser3", Gender.MALE, "1997-06-05", "loopers@loopers.com"));
            likeSummaryJpaRepository.save(new LikeSummary(1L, LikeTargetType.PRODUCT));

            LikeCriteria.LikeProduct cri1 = new LikeCriteria.LikeProduct("testUser1", 1L, LikeTargetType.PRODUCT);
            LikeCriteria.LikeProduct cri2 = new LikeCriteria.LikeProduct("testUser2", 1L, LikeTargetType.PRODUCT);
            LikeCriteria.LikeProduct cri3 = new LikeCriteria.LikeProduct("testUser3", 1L, LikeTargetType.PRODUCT);

            ExecutorService executorService = Executors.newFixedThreadPool(3);
            CountDownLatch countDownLatch = new CountDownLatch(3);

            // act
            List<Future<LikeResult>> futures = new ArrayList<>();

            // 첫 번째 좋아요
            futures.add(executorService.submit(() -> {
                try {
                    return likeFacade.likeProduct(cri1);
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 두 번째 좋아요
            futures.add(executorService.submit(() -> {
                try {
                    return likeFacade.likeProduct(cri2);
                } finally {
                    countDownLatch.countDown();
                }
            }));

            // 세 번째 좋아요
            futures.add(executorService.submit(() -> {
                try {
                    return likeFacade.likeProduct(cri3);
                } finally {
                    countDownLatch.countDown();
                }
            }));

            countDownLatch.await();

            // assert
            long successfulOrders = futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .count();

            assertAll(
                () -> assertThat(successfulOrders).isEqualTo(3),
                () -> assertThat(likeJpaRepository.count()).isEqualTo(3),
                () -> assertThat(likeSummaryJpaRepository.count()).isEqualTo(1),
                () -> assertThat(likeSummaryJpaRepository.findById(1L)).isPresent(),
                () -> assertThat(likeSummaryJpaRepository.findById(1L).get().getLikeCount()).isEqualTo(3L)
            );
        }
    }
}
