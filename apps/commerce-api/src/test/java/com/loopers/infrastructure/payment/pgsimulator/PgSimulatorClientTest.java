package com.loopers.infrastructure.payment.pgsimulator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest
class PgSimulatorClientTest {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockitoBean
    private PgSimulatorClient pgSimulatorClient;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("pg-simulator");
    }

    @AfterEach
    void tearDown() {
        circuitBreaker.reset();
    }

    @Test
    @DisplayName("700ms 이상 느린 호출 비율이 50%를 넘으면 서킷이 OPEN된다")
    void givenSlowCalls_whenExceedsSlowCallRateThreshold_thenTransitionsToOpen() throws Exception {
        // arrange
        // 800ms 걸리는 느린 호출
        when(pgSimulatorClient.request(null, null)).thenAnswer(invocation -> {
            Thread.sleep(800);
            return null;
        });

        // act & assert
        // 최소 호출 수(3)를 만족시키기 위해 3번 호출
        // 느린 호출 2회 (실패로 기록)
        circuitBreaker.executeCallable(() -> pgSimulatorClient.request(null, null));
        circuitBreaker.executeCallable(() -> pgSimulatorClient.request(null, null));

        // 빠른 호출 (성공으로 기록)
        // Mockito 설정을 바꿔서 100ms 걸리는 빠른 호출 시뮬레이션
        when(pgSimulatorClient.request(null, null)).thenAnswer(invocation -> {
            Thread.sleep(100);
            return null;
        });
        circuitBreaker.executeCallable(() -> pgSimulatorClient.request(null, null));

        // 총 3번 호출, 그 중 2번이 느린 호출(실패). 느린 호출 비율 66.6% > 50%
        // 서킷은 OPEN 상태가 되어야 함
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        // OPEN 상태이므로 추가 호출은 차단되어야 함
        assertThrows(CallNotPermittedException.class,
            () -> circuitBreaker.executeCallable(() -> pgSimulatorClient.request(null, null)));
    }

    @Test
    @DisplayName("HALF_OPEN 상태에서 허용된 1개의 호출이 성공하면 CLOSED로 복구된다")
    void givenHalfOpen_whenSingleCallSucceeds_thenTransitionsToClosed() throws Exception {
        // arrange
        circuitBreaker.transitionToOpenState();
        circuitBreaker.transitionToHalfOpenState();
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
        given(pgSimulatorClient.request(null, null)).willReturn(null);

        // act
        recordSuccess();

        // assert
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    @DisplayName("HALF_OPEN 상태에서 허용된 1개의 호출이 실패하면 다시 OPEN으로 돌아간다")
    void givenHalfOpen_whenSingleCallFails_thenTransitionsBackToOpen() {
        // arrange
        circuitBreaker.transitionToOpenState();
        circuitBreaker.transitionToHalfOpenState();
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
        given(pgSimulatorClient.request(null, null)).willThrow(new RuntimeException("Still failing"));

        // act
        recordFailure();

        // assert
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    private void recordSuccess() {
        try {
            circuitBreaker.executeCallable(() -> pgSimulatorClient.request(null, null));
        } catch (Exception e) {
            // Should not happen in success case
        }
    }

    private void recordFailure() {
        try {
            circuitBreaker.executeCallable(() -> pgSimulatorClient.request(null, null));
        } catch (Exception e) {
            // Expected
        }
    }
}
