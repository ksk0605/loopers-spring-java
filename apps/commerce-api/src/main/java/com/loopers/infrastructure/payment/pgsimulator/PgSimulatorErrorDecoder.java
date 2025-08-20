package com.loopers.infrastructure.payment.pgsimulator;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.infrastructure.payment.pgsimulator.exception.PgSimulatorException;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorApiResponse;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PgSimulatorErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String responseBody;
        try (Reader bodyIs = response.body().asReader(Charset.forName("UTF-8"))) {
            responseBody = Util.toString(bodyIs);
        } catch (IOException e) {
            log.error("PG 시뮬레이터 응답 읽기 실패", e);
            return defaultErrorDecoder.decode(methodKey, response);
        }

        try {
            PgSimulatorApiResponse<?> errorResponse = objectMapper.readValue(responseBody,
                PgSimulatorApiResponse.class);

            if (errorResponse != null && errorResponse.meta() != null
                && errorResponse.meta().result() == PgSimulatorApiResponse.Metadata.Result.FAIL) {
                String errorCode = errorResponse.meta().errorCode();
                String errorMessage = errorResponse.meta().message();
                log.warn("PG 시뮬레이터 에러 - 코드: {}, 메시지: {}", errorCode, errorMessage);
                return new PgSimulatorException(errorCode, errorMessage, response.status());
            }
        } catch (IOException e) {
            log.error("PG 시뮬레이터 응답 파싱 실패", e);
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
