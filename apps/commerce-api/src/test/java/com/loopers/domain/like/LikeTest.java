package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LikeTest {

    @DisplayName("좋아요를 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("필요한 정보가 모두 주어지면, 정상적으로 생성된다.")
        @Test
        void like_whenValidLikeInfoProvided() {
            // arrange
            Long userId = 1L;
            Long targetId = 1L;
            LikeTargetType type = LikeTargetType.PRODUCT;

            // act
            Like like = new Like(
                userId,
                targetId,
                type
            );

            // assert
            assertAll(
                () -> assertThat(like.getUserId()).isEqualTo(userId),
                () -> assertThat(like.getTarget().id()).isEqualTo(targetId),
                () -> assertThat(like.getTarget().type()).isEqualTo(type)
            );
        }

        @DisplayName("유저 ID가 null 이면, 예외를 발생한다.")
        @Test
        void throwsException_whenUserIdIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Like(
                    null,
                    1L,
                    LikeTargetType.PRODUCT
                );
            });
        }

        @DisplayName("타겟 ID가 null 이면, 예외를 발생한다.")
        @Test
        void throwsException_whenTargetIdIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Like(
                    1L,
                    null,
                    LikeTargetType.PRODUCT
                );
            });
        }

        @DisplayName("타겟 타입이 null 이면, 예외를 발생한다.")
        @Test
        void throwsException_whenTypeIsNull() {
            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new Like(
                    1L,
                    1L,
                    null
                );
            });
        }
    }
}
