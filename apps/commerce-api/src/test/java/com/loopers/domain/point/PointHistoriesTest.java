package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointHistoriesTest {

    @DisplayName("포인트 내역 목록이 주어졌을 때, ")
    @Nested
    class Create {
        @DisplayName("현재 잔액을 계산할 수 있다.")
        @Test
        void getBalance() {
            // arrange
            PointHistories histories = new PointHistories(List.of(
                new PointHistory("userId", 1000, 0, PointHistoryType.EARN),
                new PointHistory("userId", 10000, 1000, PointHistoryType.EARN),
                new PointHistory("userId", 3000, 11000, PointHistoryType.EARN)
            ));

            // act
            int balance = histories.getBalance();

            // assert
            assertThat(balance).isEqualTo(14000);
        }
    }
}
