package com.loopers.interfaces.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.function.Function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserV1ApiE2ETest {

    private static final String ENDPOINT_POST = "/api/v1/users";
    private static final Function<String, String> ENDPOINT_GET = id -> "/api/v1/users/" + id;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Post {
        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserInfo_whenValidUserInfoIsProvided() {
            // arrange
            String userId = "testUser";
            Gender gender = Gender.MALE;
            String date = "1997-06-05";
            String email = "test@loopers.com";
            UserV1Dto.CreateUserRequest request = new UserV1Dto.CreateUserRequest(
                userId,
                gender,
                date,
                email
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(ENDPOINT_POST, HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().userId()).isEqualTo(userId),
                () -> assertThat(response.getBody().data().gender()).isEqualTo(gender),
                () -> assertThat(response.getBody().data().birthDate()).isEqualTo(LocalDate.parse(date)),
                () -> assertThat(response.getBody().data().email()).isEqualTo(email)
            );
        }
    }

    @DisplayName("회원 가입 시에 성별이 없을 경우, 400 Bad Request 응답을 반환한다.")
    @Test
    void throwsBadRequest_whenGenderIsNotProvided() {
        // arrange
        String userId = "testUser";
        String date = "1997-06-05";
        String email = "test@loopers.com";
        UserV1Dto.CreateUserRequest request = new UserV1Dto.CreateUserRequest(
            userId,
            null,
            date,
            email
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // act
        ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
            testRestTemplate.exchange(ENDPOINT_POST, HttpMethod.POST, new HttpEntity<>(request, headers), responseType);

        // assert
        assertAll(
            () -> assertTrue(response.getStatusCode().is4xxClientError()),
            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
        );
    }

    @DisplayName("GET /api/v1/users/{id}")
    @Nested
    class Get {
        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserInfo_whenValidIdIsProvided() {
            // arrange
            String userId = "testUser";
            User user = userJpaRepository.save(
                new User(
                    userId,
                    Gender.MALE,
                    "1997-06-05",
                    "test@loopers.com"
                )
            );

            String requestUrl = ENDPOINT_GET.apply(userId);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().id()).isEqualTo(user.getId()),
                () -> assertThat(response.getBody().data().userId()).isEqualTo(user.getUserId()),
                () -> assertThat(response.getBody().data().gender()).isEqualTo(user.getGender()),
                () -> assertThat(response.getBody().data().birthDate()).isEqualTo(user.getBirthDate()),
                () -> assertThat(response.getBody().data().email()).isEqualTo(user.getEmail())
            );
        }

        @DisplayName("존재하지 않는 ID 로 조회할 경우, 404 Not Found 응답을 반환한다.")
        @Test
        void throwsException_whenInvalidIdIsProvided() {
            // arrange
            String invalidUserId = "useruser";
            String requestUrl = ENDPOINT_GET.apply(invalidUserId);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
            );
        }
    }
}
