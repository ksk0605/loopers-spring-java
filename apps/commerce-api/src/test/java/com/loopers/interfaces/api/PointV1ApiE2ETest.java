package com.loopers.interfaces.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.utils.DatabaseCleanUp;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointV1ApiE2ETest {

    private static final String ENDPOINT_GET = "/api/v1/points";
    private static final String ENDPOINT_POST = "/api/v1/points";

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

    @DisplayName("GET /api/v1/points")
    @Nested
    class GET {
        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.")
        @Test
        void returnsPointInfo_whenValidIdIsProvided() {
            // arrange
            String userId = "testUser";
            userJpaRepository.save(
                new User(
                    "testUser",
                    Gender.MALE,
                    "1997-06-05",
                    "test@loopers.com"
                )
            );
            int point = 0;

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                testRestTemplate.exchange(ENDPOINT_GET, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().point()).isEqualTo(point)
            );
        }

        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        @Test
        void throwsException_whenUserIdIsNotProvided() {
            // arrange
            HttpHeaders headers = new HttpHeaders();

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                testRestTemplate.exchange(ENDPOINT_GET, HttpMethod.GET, new HttpEntity<>(null, headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }
    }

    @DisplayName("POST /api/v1/points")
    @Nested
    class Post {
        @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
        @Test
        void returnsChargedPoint_whenValidRequestIsProvided() {
            // arrange
            String userId = "testUser";
            userJpaRepository.save(
                new User(
                    "testUser",
                    Gender.MALE,
                    "1997-06-05",
                    "test@loopers.com"
                )
            );
            int chargeAmount = 1000;
            PointV1Dto.ChargePointRequest request = new PointV1Dto.ChargePointRequest(
                chargeAmount
            );

            String requestUrl = ENDPOINT_POST + "/charge";
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(request, headers),
                    responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().point()).isEqualTo(chargeAmount)
            );
        }

        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 BAD_REQUEST 응답을 반환한다.")
        @Test
        void throwsException_whenUserIdIsNotProvided() {
            // arrange
            int chargeAmount = 1000;
            PointV1Dto.ChargePointRequest request = new PointV1Dto.ChargePointRequest(
                chargeAmount
            );

            String requestUrl = ENDPOINT_POST + "/charge";
            HttpHeaders headers = new HttpHeaders();

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(request, headers),
                    responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );
        }

        @DisplayName("존재하지 않는 유저로 요청할 경우, 404 NOT_FOUND 응답을 반환한다.")
        @Test
        void throwsException_whenInvalidUserIdIsProvided() {
            // arrange
            String invalidUserId = "testUser";

            int chargeAmount = 1000;
            PointV1Dto.ChargePointRequest request = new PointV1Dto.ChargePointRequest(
                chargeAmount
            );

            String requestUrl = ENDPOINT_POST + "/charge";
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", invalidUserId);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.POST, new HttpEntity<>(request, headers),
                    responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
            );
        }
    }
}
