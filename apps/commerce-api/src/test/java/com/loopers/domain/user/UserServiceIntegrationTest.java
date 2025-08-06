package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

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
            UserCommand.Create command = new UserCommand.Create(userId, gender.name(), birthDate, email);

            // act
            User user = userService.createUser(command);

            // assert
            assertAll(
                () -> verify(userJpaRepository, times(1)).save(any(User.class)),
                () -> assertThat(user.getUserId()).isEqualTo(userId),
                () -> assertThat(user.getGender()).isEqualTo(gender),
                () -> assertThat(user.getBirthDate()).isEqualTo(LocalDate.parse(birthDate)),
                () -> assertThat(user.getEmail()).isEqualTo(email)
            );
        }

        @DisplayName("이미 가입된 ID라면, CONFLICT 예외가 발생한다.")
        @Test
        void throwsException_whenDuplicateIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userService.createUser(new UserCommand.Create("testUser", "FEMALE", "1998-06-05", "test2@loopers.com"));
            });

            // assert
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
        }
    }

    @DisplayName("유저 조회 시, ")
    @Nested
    class Get {
        @DisplayName("해당 ID의 유저가 존재하는 경우, 회원 정보가 반환된다.")
        @Test
        void returnsExampleInfo_whenValidIdIsProvided() {
            // arrange
            String userId = "testUser";
            userJpaRepository.save(
                new User(userId, Gender.MALE, "1997-06-05", "test@loopers.com")
            );

            // act
            User user = userService.get(userId);

            // assert
            assertThat(user.getUserId()).isEqualTo(userId);
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, NOT FOUND 예외를 발생한다.")
        @Test
        void throwsException_whenInvalidIdIsProvided() {
            // arrange
            userJpaRepository.save(
                new User("testUser", Gender.MALE, "1997-06-05", "test@loopers.com")
            );
            String invalidUserId = "useruser";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                userService.get(invalidUserId);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
