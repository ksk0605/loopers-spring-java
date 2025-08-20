package com.loopers.infrastructure.payment.pgsimulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.loopers.infrastructure.payment.pgsimulator.request.PgSimulatorPaymentRequest;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorApiResponse;
import com.loopers.infrastructure.payment.pgsimulator.response.PgSimulatorTransactionResponse;

@FeignClient(name = "pg-simulator", url = "http://localhost:8082", configuration = PgSimulatorConfig.class)
public interface PgSimulatorClient {
    @PostMapping("/api/v1/payments")
    PgSimulatorApiResponse<PgSimulatorTransactionResponse> request(@RequestBody PgSimulatorPaymentRequest request, @RequestHeader("X-USER-ID") String userId);
}
