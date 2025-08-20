package com.loopers.interfaces.api.order;

import com.loopers.domain.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Order V1 API", description = "주문 API 입니다.")
public interface OrderV1ApiSpec {
    @Operation(
        summary = "주문 생성",
        description = "주문을 생성합니다."
    )
    ApiResponse<OrderV1Dto.OrderResponse> createOrder(
        OrderV1Dto.OrderRequest request,
        UserInfo userInfo
    );

    @Operation(
        summary = "주문 목록 조회",
        description = "주문 목록을 조회합니다."
    )
    ApiResponse<OrderV1Dto.OrderResponses> getOrders(UserInfo userInfo);

    @Operation(
        summary = "주문 조회",
        description = "주문을 조회합니다."
    )
    ApiResponse<OrderV1Dto.OrderResponse> getOrder(
        UserInfo userInfo,
        Long orderId
    );
}
