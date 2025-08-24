package com.loopers.interfaces.api.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserResult;
import com.loopers.domain.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderRequest;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderResponses;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderV1Controller implements OrderV1ApiSpec {

    private final OrderFacade orderFacade;
    private final UserFacade userFacade;

    @PostMapping
    @Override
    public ApiResponse<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            UserInfo userInfo) {
        var criteria = request.toOrderCriteria(userInfo.id(), userInfo.userId());
        OrderResult result = orderFacade.order(criteria);
        return ApiResponse.success(OrderResponse.from(result));
    }

    @GetMapping
    @Override
    public ApiResponse<OrderResponses> getOrders(UserInfo userInfo) {
        UserResult user = userFacade.getUser(userInfo.userId());
        var results = orderFacade.getOrders(user.id());
        return ApiResponse.success(OrderResponses.from(results));
    }

    @GetMapping("/{orderId}")
    @Override
    public ApiResponse<OrderResponse> getOrder(UserInfo userInfo, @PathVariable Long orderId) {
        UserResult user = userFacade.getUser(userInfo.userId());
        var result = orderFacade.getOrder(orderId, user.id());
        return ApiResponse.success(OrderResponse.from(result));
    }
}
