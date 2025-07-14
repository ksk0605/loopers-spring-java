package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PointHistoryTest {

    @DisplayName("포인트 충전할 때, ")
    @Nested
    class Create {
        @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void throwsBadRequestException_whenAmountIsBelowZero(int amount) {
            // arrange
            String userId = "";
            int newBalance = 0;
            PointHistoryType type = PointHistoryType.EARN;

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new PointHistory(userId, amount, newBalance, type);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
