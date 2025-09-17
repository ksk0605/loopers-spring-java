package com.loopers.interfaces.api.rank;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ranking V1 API", description = "랭킹 관련 API 입니다.")
public interface RankV1ApiSpec {
    @Operation(
        summary = "상품 랭킹 순 목록 조회",
        description = "기간 키(periodKey), 기간 타입(periodType)을 기반으로 일간,주간,월간 상품 랭킹을 조회합니다."
    )
    ApiResponse<ProductV1Dto.ProductsResponse> getRankingProducts(
        String periodKey,
        String periodType,
        Integer page,
        Integer size
    );
}
