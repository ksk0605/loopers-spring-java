package com.loopers.interfaces.api.user;

import org.springframework.web.bind.annotation.RequestHeader;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "User 관련 API 입니다.")
public interface UserV1ApiSpec {
    @Operation(
        summary = "유저 회원 가입",
        description = "회원 가입에 필요한 정보로 가입을 시도합니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "회원 가입 성공",
            content = @Content(schema = @Schema(implementation = UserV1Dto.UserResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "중복된 사용자 ID"
        )
    })
    ApiResponse<UserV1Dto.UserResponse> createUser(
        @RequestBody(
            description = "회원 가입 요청 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = UserV1Dto.CreateUserRequest.class))
        ) UserV1Dto.CreateUserRequest request
    );

    @Operation(
        summary = "내 정보 조회",
        description = "X-USER-ID 헤더로 유저를 식별하여 내 정보를 조회합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> getMe(
        @RequestHeader(
            name = "X-USER-ID",
            required = true
        ) String userId
    );
}
