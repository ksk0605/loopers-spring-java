package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.like.LikeSummaryJpaRepository;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class LikeServiceIntegrationTest {

    @Autowired
    private LikeService likeService;

    @MockitoSpyBean
    private LikeJpaRepository likeJpaRepository;

    @MockitoSpyBean
    private LikeSummaryJpaRepository likeSummaryJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("좋아요를 추가 할 때, ")
    @Nested
    class Add {
        @DisplayName("모든 정보가 주어지면, 타겟에 좋아요 처리를 한다.")
        @Test
        void addLike_whenValidLikeInfoProvided() {
            // act
            likeService.like(1L, 1L, LikeTargetType.PRODUCT);

            // assert
            Optional<Like> like = likeJpaRepository.findById(1L);
            assertAll(
                () -> verify(likeJpaRepository, times(1)).save(any(Like.class)),
                () -> verify(likeSummaryJpaRepository, times(1)).save(any(LikeSummary.class)),
                () -> assertThat(like).isPresent(),
                () -> assertThat(like.get().getUserId()).isEqualTo(1L),
                () -> assertThat(like.get().getTarget().id()).isEqualTo(1L),
                () -> assertThat(like.get().getTarget().type()).isEqualTo(LikeTargetType.PRODUCT));
        }

        @DisplayName("이미 유저의 타겟에 대한 좋아요가 있으면, 아무것도 하지 않는다.")
        @Test
        void doesNothing_whenLikeAlreadyExists() {
            // arrange
            likeJpaRepository.save(new Like(1L, 1L, LikeTargetType.PRODUCT));

            // act
            likeService.like(1L, 1L, LikeTargetType.PRODUCT);

            // assert
            List<Like> likes = likeJpaRepository.findAll();
            assertAll(
                () -> verify(likeJpaRepository, times(1)).save(any(Like.class)),
                () -> verify(likeSummaryJpaRepository, times(0)).save(any(LikeSummary.class)),
                () -> assertThat(likes).hasSize(1));
        }
    }

    @DisplayName("좋아요를 취소 할 때, ")
    @Nested
    class Cancel {
        @DisplayName("모든 정보가 주어지면, 타겟에 좋아요 취소 처리를 한다.")
        @Test
        void cancelLike_whenValidLikeInfoProvided() {
            // arrange
            likeJpaRepository.save(new Like(1L, 1L, LikeTargetType.PRODUCT));

            // act
            likeService.unlike(1L, 1L, LikeTargetType.PRODUCT);

            // assert
            Optional<Like> like = likeJpaRepository.findById(1L);
            assertAll(
                () -> verify(likeJpaRepository, times(1)).deleteByUserIdAndTarget(any(Long.class),
                    any(LikeTarget.class)),
                () -> assertThat(like).isEmpty());
        }

        @DisplayName("이미 유저의 타겟에 대한 좋아요가 없으면, 아무것도 하지 않는다.")
        @Test
        void doesNothing_whenLikeDoesNotExist() {
            // act
            likeService.unlike(1L, 1L, LikeTargetType.PRODUCT);

            // assert
            List<Like> likes = likeJpaRepository.findAll();
            assertAll(
                () -> verify(likeJpaRepository, times(0)).deleteByUserIdAndTarget(any(Long.class),
                    any(LikeTarget.class)),
                () -> assertThat(likes).hasSize(0));
        }
    }

    @DisplayName("좋아요를 조회할 때, ")
    @Nested
    class Get {
        @DisplayName("타겟 ID와 타겟 타입이 주어지면, 해당 타입의 좋아요 숫자를 반환한다.")
        @Test
        void returnsLikeCount_whenTargetIdAndTargetTypeProvided() {
            // arrange
            likeJpaRepository.save(new Like(1L, 1L, LikeTargetType.PRODUCT));

            // act
            Long likeCount = likeService.count(1L, LikeTargetType.PRODUCT);

            // assert
            assertThat(likeCount).isEqualTo(1);
        }
    }

    @DisplayName("내가 좋아요 한 타겟 목록을 조회할 때, ")
    @Nested
    class GetAll {
        @DisplayName("타겟 ID와 타겟 타입이 주어지면, 해당 타입의 좋아요 목록을 반환한다.")
        @Test
        void returnsLikeCount_whenTargetIdAndTargetTypeProvided() {
            // arrange
            likeJpaRepository.save(new Like(1L, 1L, LikeTargetType.PRODUCT));
            likeJpaRepository.save(new Like(1L, 2L, LikeTargetType.BRAND));
            likeJpaRepository.save(new Like(1L, 3L, LikeTargetType.PRODUCT));

            // act
            List<LikeInfo> likeInfos = likeService.getAll(1L, LikeTargetType.PRODUCT);

            // assert
            assertAll(
                () -> assertThat(likeInfos).hasSize(2),
                () -> assertThat(likeInfos.get(0).targetId()).isEqualTo(1L),
                () -> assertThat(likeInfos.get(0).targetType()).isEqualTo(LikeTargetType.PRODUCT),
                () -> assertThat(likeInfos.get(1).targetId()).isEqualTo(3L),
                () -> assertThat(likeInfos.get(1).targetType()).isEqualTo(LikeTargetType.PRODUCT)
            );
        }
    }
}
