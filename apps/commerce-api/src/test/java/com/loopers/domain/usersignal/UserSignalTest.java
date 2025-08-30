package com.loopers.domain.usersignal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserSignalTest {
    @DisplayName("유저시그널은 좋아요 0, 조회수 0으로 생성된다.")
    @Test
    void create() {
        // arrange
        UserSignal userSignal = new UserSignal(1L, TargetType.PRODUCT);

        // act & assert
        assertThat(userSignal.getLikeCount()).isEqualTo(0);
        assertThat(userSignal.getViews()).isEqualTo(0);
    }

    @DisplayName("유저시그널의 좋아요 수는 0보다 작을 수 없다.")
    @Test
    void updateLikeCount_whenLikeCountIsNegative() {
        // arrange
        UserSignal userSignal = new UserSignal(1L, TargetType.PRODUCT);

        // act & assert
        assertThrows(IllegalStateException.class, () -> userSignal.updateLikeCount(-1L));
    }
}
