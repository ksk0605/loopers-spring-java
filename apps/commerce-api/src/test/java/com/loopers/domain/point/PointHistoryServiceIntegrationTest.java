package com.loopers.domain.point;

import static com.loopers.support.fixture.UserFixture.anUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.domain.user.User;
import com.loopers.infrastructure.point.PointHistoryJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.IntegrationTest;

@SpringBootTest
class PointHistoryServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private PointHistoryService pointHistoryService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    @MockitoSpyBean
    private PointHistoryJpaRepository pointHistoryJpaRepository;

    @DisplayName("포인트 내역을 추가할 때, ")
    @Nested
    class Create {
        @DisplayName("주어진 유저 ID와 포인트 Amount로 저장된다.")
        @Test
        void createPointHistory_whenUserIdAndPointAmountArdProvided() {
            // arrange
            User user = userJpaRepository.save(anUser().build());
            pointHistoryJpaRepository.save(new PointHistory(user.getUserId(), 1000, 0, PointHistoryType.EARN));

            // act
            PointHistory history = pointHistoryService.earn(user.getUserId(), 100);

            // assert
            assertAll(
                () -> verify(pointHistoryJpaRepository, times(2)).save(any(PointHistory.class)),
                () -> assertThat(history.getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(history.getAmount()).isEqualTo(100),
                () -> assertThat(history.getBalance()).isEqualTo(1100),
                () -> assertThat(history.getType()).isEqualTo(PointHistoryType.EARN)
            );
        }
    }
}
