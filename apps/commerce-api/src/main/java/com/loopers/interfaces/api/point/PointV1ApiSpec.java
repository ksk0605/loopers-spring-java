package com.loopers.interfaces.api.point;

import org.springframework.web.bind.annotation.RequestHeader;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;

public interface PointV1ApiSpec {
    @Operation(
        summary = "보유 포인트 조회",
        description = "유저 ID로 현재 보유 포인트를 조회합니다."
    )
    ApiResponse<PointV1Dto.PointResponse> getPoint(
        @RequestHeader(
            name = "X-USER-ID",
            required = true
        ) String userId
    );
}
