package com.loopers.application.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
class PointFacadeIntegrationTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트 조회를 시도할 때, ")
    @Nested
    class Get {

        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnsMyPoints_whenValidUserIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            PointInfo pointInfo = pointFacade.getMyPoint("testUser");

            // assert
            assertThat(pointInfo.point()).isEqualTo(0);
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, NOT_FOUND 예외가 발생한다.")
        @Test
        void throwsException_whenInvalidUserIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                pointFacade.getMyPoint("testtest");
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }

    @DisplayName("포인트 충전을 시도할 때, ")
    @Nested
    class Create {
        @DisplayName("충전 가능한 액수로 시도한 경우, 포인트를 정상적으로 충전한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 1000})
        void createPointHistory_whenValidAmountIsProvided(int amount) {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            PointInfo pointInfo = pointFacade.chargePoint("testUser", amount);

            // assert
            assertThat(pointInfo.point()).isEqualTo(amount);
        }

        @DisplayName("존재하지 않는 유저 ID 로 충전할 경우, NOT_FOUND 예외가 발생한다.")
        @Test
        void throwsException_whenInvalidUserIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                pointFacade.chargePoint("testtest", 1000);
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
