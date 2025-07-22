package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class UserTest {
    @DisplayName("유저를 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("필요한 정보가 모두 주어지면, 정상적으로 생성된다.")
        @Test
        void createsUser_whenValidUserInfosAreProvided() {
            // arrange
            String userId = "testUser";
            Gender gender = Gender.MALE;
            String birthDate = "1990-01-01";
            String email = "test@test.com";

            // act
            User user = new User(userId, gender, birthDate, email);

            // assert
            assertAll(
                () -> assertThat(user.getUserId()).isNotNull(),
                () -> assertThat(user.getGender()).isEqualTo(Gender.MALE),
                () -> assertThat(user.getBirthDate()).isEqualTo(LocalDate.parse(birthDate)),
                () -> assertThat(user.getEmail()).isEqualTo(email),
                () -> assertThat(user.getPoint()).isEqualTo(0)
            );
        }

        @DisplayName("ID가 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenIdIsBlank() {
            // arrange
            String userId = "    ";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User(userId, Gender.MALE, "1990-01-01", "test@test.com");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("ID에 영문 및 숫자 외의 문자가 들어오면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"한글", "@"})
        void throwsBadRequestException_whenIdContainsNonAlphanumericCharacters(String source) {
            // arrange
            String userId = "testId" + source;

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User(userId, Gender.MALE, "1990-01-01", "test@test.com");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("ID가 10자를 넘어가면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenIdExceeds10Characters() {
            // arrange
            String userId = "12overTenId";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User(userId, Gender.MALE, "1990-01-01", "test@test.com");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("이메일이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenEmailIsBlank() {
            // arrange
            String email = "  ";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User("testUser66", Gender.MALE, "1990-01-01", email);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "test.test.com", // @ 없음
            "test@", // 도메인 없음
            "test@test" // 최상위 도메인 없음
        })
        void throwsBadRequestException_whenEmailFormatIsInvalid(String value) {
            // arrange
            String email = value;

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User("testUser66", Gender.MALE, "1990-01-01", email);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("생년월일이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenBirthDateIsBlank() {
            // arrange
            String birthDate = "  ";

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User("testUser", Gender.MALE, birthDate, "test@test.com");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "1990/01/01", // 슬래시 사용
            "01-01-1990", // MM-dd-yyyy 형식
            "1990.01.01", // 점 사용
            "1990-1-1", // 한 자리 월/일
            "1990-01-1", // 한 자리 일
            "1990-1-01" // 한 자리 월
        })
        void throwsBadRequestException_whenBirthDateFormatIsInvalid(String value) {
            // arrange
            String birthDate = value;

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User("testUser", Gender.MALE, birthDate, "test@test.com");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("생년월일이 존재하지 않는 날짜라면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "2010-02-30",
            "1990-12-32",
            "1990-01-00",
        })
        void throwsBadRequestException_whenBirthDateIsInvalid(String value) {
            // arrange
            String birthDate = value;

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                new User("testUser", Gender.MALE, birthDate, "test@test.com");
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }
}
