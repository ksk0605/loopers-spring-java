package com.loopers.interfaces.api.point;

import org.springframework.web.bind.annotation.RequestHeader;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(
        summary = "포인트 충전",
        description = "요청한 수치만큼의 포인트를 충전합니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "포인트 충전 성공",
            content = @Content(schema = @Schema(implementation = PointV1Dto.PointResponse.class))
        )
    })
    ApiResponse<PointV1Dto.PointResponse> chargePoint(
        @RequestHeader(
            name = "X-USER-ID",
            required = true
        ) String userId,
        @RequestBody(
            description = "포인트 충전 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = PointV1Dto.ChargePointRequest.class))
        ) PointV1Dto.ChargePointRequest request
    );
}
