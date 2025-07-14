package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @MockitoSpyBean
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원 가입시, ")
    @Nested
    class Create {
        @DisplayName("유저 저장이 수행된다.")
        @Test
        void createUser() {
            // arrange
            String userId = "testUser";
            Gender gender = Gender.MALE;
            String birthDate = "1997-06-05";
            String email = "test@loopers.com";

            // act
            userService.createUser(userId, gender, birthDate, email);

            // assert
            verify(userJpaRepository, times(1)).save(any(User.class));
        }

        @DisplayName("이미 가입된 ID라면, CONFLICT 예외가 발생한다.")
        @Test
        void throwsException_whenDuplicateIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );
            String userId = "testUser";
            Gender gender = Gender.FEMALE;
            String birthDate = "1998-06-05";
            String email = "test2@loopers.com";

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userService.createUser(userId, gender, birthDate, email);
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }
}
