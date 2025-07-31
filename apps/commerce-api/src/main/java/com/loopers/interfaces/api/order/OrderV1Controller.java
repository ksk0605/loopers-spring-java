package com.loopers.interfaces.api.order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.order.OrderCommand;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderRequest;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderResponse;

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
        @RequestHeader(name = "X-USER-ID", required = true) String userId,
        @RequestBody OrderRequest request) {
        var orderOptions = request.toOrderOptions();
        UserInfo user = userFacade.getUser(userId);
        OrderCommand.Place command = new OrderCommand.Place(user.id(), orderOptions);
        OrderResult result = orderFacade.placeOrder(command);
        return ApiResponse.success(OrderResponse.from(result));
    }
}
