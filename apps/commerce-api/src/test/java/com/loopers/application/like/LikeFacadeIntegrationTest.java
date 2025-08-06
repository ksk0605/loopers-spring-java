package com.loopers.application.like;

import static com.loopers.support.fixture.BrandFixture.aBrand;
import static com.loopers.support.fixture.LikeFixture.aLike;
import static com.loopers.support.fixture.ProductFixture.aProduct;
import static com.loopers.support.fixture.UserFixture.anUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.application.product.ProductResult;
import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.like.LikeSummaryJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.IntegrationTest;

@SpringBootTest
class LikeFacadeIntegrationTest extends IntegrationTest {
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

    @DisplayName("좋아요를 추가 할 때, ")
    @Nested
    class GetLikedProducts {
        @DisplayName("내가 좋아요한 상품 목록을 조회할 때, 해당 타입의 좋아요 목록을 반환한다.")
        @Test
        void returnsProductResult_whenTargetTypeProvided() {
            // arrange
            userJpaRepository.save(anUser().build());

            brandJpaRepository.save(aBrand().build());

            productJpaRepository.save(aProduct().name("상품1").build());
            productJpaRepository.save(aProduct().name("상품2").build());
            productJpaRepository.save(aProduct().name("상품3").build());
            productJpaRepository.save(aProduct().name("상품4").build());

            likeJpaRepository.save(aLike().targetId(1L).build());
            likeJpaRepository.save(aLike().targetId(2L).targetType(LikeTargetType.BRAND).build());
            likeJpaRepository.save(aLike().targetId(3L).build());
            likeJpaRepository.save(aLike().targetId(4L).build());

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
            brandJpaRepository.save(aBrand().build());
            productJpaRepository.save(aProduct().build());
            userJpaRepository.save(anUser().userId("testUser1").build());
            userJpaRepository.save(anUser().userId("testUser2").build());
            userJpaRepository.save(anUser().userId("testUser3").build());
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
