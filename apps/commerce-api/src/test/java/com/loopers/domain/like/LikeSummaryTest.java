package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LikeSummaryTest {

    @DisplayName("좋아요 숫자를 올릴 때, ")
    @Nested
    class Increment {
        @DisplayName("숫자가 1 증가한다.")
        @Test
        void incrementLikeCount() {
            // arrange
            LikeSummary likeSummary = new LikeSummary(1L, LikeTargetType.PRODUCT);

            // act
            likeSummary.incrementLikeCount();

            // assert
            assertThat(likeSummary.getLikeCount()).isEqualTo(1);
        }
    }

    @DisplayName("좋아요 숫자를 내릴 때, ")
    @Nested
    class Decrement {
        @DisplayName("숫자가 0 미만이면 예외를 발생한다.")
        @Test
        void decrementLikeCount_failWhenCountUnderZero() {
            // arrange
            LikeSummary likeSummary = new LikeSummary(1L, LikeTargetType.PRODUCT);

            // act
            var exception = assertThrows(IllegalArgumentException.class, () ->
                likeSummary.decrementLikeCount()
            );

            // assert
            assertThat(exception.getMessage()).isEqualTo("좋아요 카운트는 0 이상이어야 합니다.");
        }

        @DisplayName("좋아요 카운트가 1이상이면 정상적으로 카운트를 1 내린다.")
        @Test
        void decrementLikeCount_successWhenCountOverOne() {
            // arrange
            LikeSummary likeSummary = new LikeSummary(1L, LikeTargetType.PRODUCT);

            // act
            likeSummary.incrementLikeCount();
            likeSummary.decrementLikeCount();

            // assert
            assertThat(likeSummary.getLikeCount()).isEqualTo(0);
        }
    }
}
