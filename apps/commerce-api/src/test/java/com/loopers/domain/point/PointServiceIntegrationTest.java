package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
public class PointServiceIntegrationTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("보유 포인트를 조회할 때, ")
    @Nested
    class Get {
        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnsMyPoints_whenValidUserIdIsProvided() {
            // arrange
            String userId = "testUser";
            userJpaRepository.save(
                new User(userId, Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            int myPoint = pointService.getMyPoint(userId);

            // assert
            assertThat(myPoint).isEqualTo(0);
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, NOT_FOUND 예외가 발생한다.")
        @Test
        void throwsException_whenInvalidUserIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );
            String invalidUserId = "testtest";

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                pointService.getMyPoint(invalidUserId);
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
