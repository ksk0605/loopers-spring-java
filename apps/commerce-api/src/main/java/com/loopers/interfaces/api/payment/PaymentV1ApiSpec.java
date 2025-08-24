package com.loopers.interfaces.api.payment;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment V1 API", description = "Payment 관련 API 입니다.")
public interface PaymentV1ApiSpec {
    @Operation(
        summary = "결제 요청",
        description = "결제를 요청합니다."
    )
    ApiResponse<PaymentV1Dto.PaymentResponse> requestPayment(
        String userId,
        @RequestBody(description = "결제 요청 정보", required = true) PaymentV1Dto.PaymentRequest request
    );
}
