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
        void throwsException_whenIdIsBlank() {
            // arrange
            String userId = "    ";

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User(userId, Gender.MALE, "1990-01-01", "test@test.com");
            });
        }

        @DisplayName("ID에 영문 및 숫자 외의 문자가 들어오면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {"한글", "@"})
        void throwsException_whenIdContainsNonAlphanumericCharacters(String source) {
            // arrange
            String userId = "testId" + source;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User(userId, Gender.MALE, "1990-01-01", "test@test.com");
            });
        }

        @DisplayName("ID가 10자를 넘어가면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsException_whenIdExceeds10Characters() {
            // arrange
            String userId = "12overTenId";

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User(userId, Gender.MALE, "1990-01-01", "test@test.com");
            });
        }

        @DisplayName("이메일이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsException_whenEmailIsBlank() {
            // arrange
            String email = "  ";

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User("testUser66", Gender.MALE, "1990-01-01", email);
            });
        }

        @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "test.test.com", // @ 없음
            "test@", // 도메인 없음
            "test@test" // 최상위 도메인 없음
        })
        void throwsException_whenEmailFormatIsInvalid(String value) {
            // arrange
            String email = value;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User("testUser66", Gender.MALE, "1990-01-01", email);
            });
        }

        @DisplayName("생년월일이 빈칸으로만 이루어져 있으면, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsException_whenBirthDateIsBlank() {
            // arrange
            String birthDate = "  ";

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User("testUser", Gender.MALE, birthDate, "test@test.com");
            });
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
        void throwsException_whenBirthDateFormatIsInvalid(String value) {
            // arrange
            String birthDate = value;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User("testUser", Gender.MALE, birthDate, "test@test.com");
            });
        }

        @DisplayName("생년월일이 존재하지 않는 날짜라면, BAD_REQUEST 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(strings = {
            "2010-02-30",
            "1990-12-32",
            "1990-01-00",
        })
        void throwsException_whenBirthDateIsInvalid(String value) {
            // arrange
            String birthDate = value;

            // act & assert
            assertThrows(IllegalArgumentException.class, () -> {
                new User("testUser", Gender.MALE, birthDate, "test@test.com");
            });
        }
    }
}
