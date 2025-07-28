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
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class LikeServiceIntegrationTest {

    @Autowired
    private LikeService likeService;

    @MockitoSpyBean
    private LikeJpaRepository likeJpaRepository;

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
                () -> assertThat(like).isPresent(),
                () -> assertThat(like.get().getUserId()).isEqualTo(1L),
                () -> assertThat(like.get().getTarget().id()).isEqualTo(1L),
                () -> assertThat(like.get().getTarget().type()).isEqualTo(LikeTargetType.PRODUCT)
            );
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
                () -> assertThat(likes).hasSize(1)
            );
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
                () -> verify(likeJpaRepository, times(1)).deleteByTarget(any(LikeTarget.class)),
                () -> assertThat(like).isEmpty()
            );
        }

        @DisplayName("이미 유저의 타겟에 대한 좋아요가 없으면, 아무것도 하지 않는다.")
        @Test
        void doesNothing_whenLikeDoesNotExist() {
            // act
            likeService.unlike(1L, 1L, LikeTargetType.PRODUCT);

            // assert
            List<Like> likes = likeJpaRepository.findAll();
            assertAll(
                () -> verify(likeJpaRepository, times(0)).deleteByTarget(any(LikeTarget.class)),
                () -> assertThat(likes).hasSize(0)
            );
        }
    }
}
