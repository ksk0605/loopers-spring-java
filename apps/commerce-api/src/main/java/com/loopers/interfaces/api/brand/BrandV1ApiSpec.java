package com.loopers.interfaces.api.brand;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Brand V1 API", description = "브랜드 관련 API 입니다.")
public interface BrandV1ApiSpec {
    @Operation(
        summary = "브랜드",
        description = "ID로 브랜드를 조회합니다."
    )
    ApiResponse<BrandV1Dto.BrandResponse> getBrand(
        @Schema(name = "브랜드 ID", description = "조회할 브랜드의 ID")
        Long brandId
    );
}
