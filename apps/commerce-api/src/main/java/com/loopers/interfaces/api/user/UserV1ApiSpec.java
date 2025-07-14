package com.loopers.interfaces.api.user;

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
            responseCode = "201",
            description = "회원 가입 성공",
            content = @Content(schema = @Schema(implementation = UserV1Dto.UserResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (필수 정보 누락, 형식 오류 등)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
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
        summary = "유저 조회",
        description = "USER_ID로 유저를 조회합니다."
    )
    ApiResponse<UserV1Dto.UserResponse> getUser(
        @Schema(name = "유저 ID", description = "조회할 유저의 USER_ID")
        String userId
    );
}
