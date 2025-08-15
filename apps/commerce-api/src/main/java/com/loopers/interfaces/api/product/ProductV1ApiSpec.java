package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "프로덕트 관련 API 입니다.")
public interface ProductV1ApiSpec {
    @Operation(
        summary = "상품 조회",
        description = "ID로 상품을 조회합니다."
    )
    ApiResponse<ProductV1Dto.ProductDetailResponse> getProduct(Long productId);

    @Operation(
        summary = "상품 목록 조회",
        description = "판매 가능한 상품 목록을 조회합니다."
    )
    ApiResponse<ProductV1Dto.ProductsResponse> getProducts(
        String sort,
        Integer page,
        Integer size
    );
}
