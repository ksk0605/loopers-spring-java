package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Like V1 API", description = "좋아요 관련 API 입니다.")
public interface LikeV1ApiSpec {
    @Operation(
        summary = "상품 좋아요",
        description = "상품 ID로 좋아요를 추가합니다."
    )
    ApiResponse<LikeV1Dto.LikeResponse> likeProduct(
        @Schema(name = "사용자 ID", description = "좋아요를 추가할 사용자의 ID")
        String userId,
        @Schema(name = "상품 ID", description = "조회할 상품의 ID")
        Long productId
    );
}
