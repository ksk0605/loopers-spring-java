package com.loopers.infrastructure.payment.pgsimulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.infrastructure.payment.pgsimulator.exception.PgSimulatorException;
import com.loopers.infrastructure.payment.pgsimulator.exception.PgSimulatorRetryableException;

import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PgSimulatorErrorDecoderTest {

    private ErrorDecoder errorDecoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        errorDecoder = new PgSimulatorErrorDecoder(objectMapper);
    }

    @Test
    @DisplayName("실패 응답(FAIL) 상태가 4xx이면, JSON 본문을 파싱하여 PgSimulatorException을 던진다")
    void givenFailResponse_whenDecode_thenThrowsPgSimulatorException() {
        // arrange 
        String errorJson = """
            {
                "meta": {
                    "result": "FAIL",
                    "errorCode": "INVALID_CARD_INFO",
                    "message": "카드 정보가 잘못되었습니다."
                },
                "data": null
            }
            """;

        Response mockResponse = createMockResponse(400, errorJson);
        String methodKey = "PgSimulatorClient#request(PgSimulatorPaymentRequest,String)";

        // act
        Exception exception = errorDecoder.decode(methodKey, mockResponse);

        // assert
        assertInstanceOf(PgSimulatorException.class, exception);

        PgSimulatorException pgException = (PgSimulatorException) exception;
        assertEquals("INVALID_CARD_INFO", pgException.getErrorCode());
        assertEquals("카드 정보가 잘못되었습니다.", pgException.getMessage());
    }

    @Test
    @DisplayName("실패 응답(FAIL) 상태가 5xx이면, JSON 본문을 파싱하여 PgSimulatorRetryableException을 던진다")
    void givenFailResponse_whenDecode_thenThrowsPgSimulatorRetryableException() {
        // arrange 
        String errorJson = """
            {
                "meta": {
                    "result": "FAIL",
                    "errorCode": "SERVER_ERROR",
                    "message": "서버에 일시적인 문제가 발생했습니다."
                },
                "data": null
            }
            """;

        Response mockResponse = createMockResponse(500, errorJson);
        String methodKey = "PgSimulatorClient#request(PgSimulatorPaymentRequest,String)";

        // act
        Exception exception = errorDecoder.decode(methodKey, mockResponse);

        // assert
        assertInstanceOf(PgSimulatorRetryableException.class, exception);

        PgSimulatorRetryableException pgException = (PgSimulatorRetryableException) exception;
        assertEquals("SERVER_ERROR", pgException.getErrorCode());
        assertEquals("서버에 일시적인 문제가 발생했습니다.", pgException.getMessage());
    }

    @Test
    @DisplayName("응답 본문이 유효하지 않은 JSON이면, 기본 ErrorDecoder로 동작을 위임한다")
    void givenMalformedJson_whenDecode_thenDelegateToDefaultDecoder() {
        // arrange
        String malformedJson = "{\"meta\":{\"result\":\"FAIL\"";

        Response mockResponse = createMockResponse(400, malformedJson);
        String methodKey = "PgSimulatorClient#request(..)";

        // act
        Exception exception = errorDecoder.decode(methodKey, mockResponse);

        // assert
        assertThat(exception).isNotInstanceOf(PgSimulatorException.class);
        assertThat(exception).isInstanceOf(feign.FeignException.class);
    }
    
    @Test
    @DisplayName("응답 본문은 JSON이지만 result 필드가 FAIL이 아니면, 기본 ErrorDecoder로 위임한다")
    void givenNotFailResponse_whenDecode_thenDelegateToDefaultDecoder() {
        // arrange
        String successJson = """
            {
                "meta": {
                    "result": "SUCCESS",
                    "errorCode": null,
                    "message": null
                },
                "data": {}
            }
            """;

        Response mockResponse = createMockResponse(400, successJson);
        String methodKey = "PgSimulatorClient#request(..)";

        // act
        Exception exception = errorDecoder.decode(methodKey, mockResponse);

        // assert
        assertThat(exception).isNotInstanceOf(PgSimulatorException.class);
        assertThat(exception).isInstanceOf(feign.FeignException.class);
    }

    /**
     * 테스트를 위한 가짜 feign.Response 객체를 생성하는 헬퍼 메소드
     * 
     * @param status
     * @param jsonBody
     * @return
     */
    private Response createMockResponse(int status, String jsonBody) {
        return Response.builder()
                .status(status)
                .reason("Bad Request")
                .request(Request.create(HttpMethod.POST, "/api/v1/payments", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .headers(Collections.emptyMap())
                .body(jsonBody, StandardCharsets.UTF_8)
                .build();
    }
}
