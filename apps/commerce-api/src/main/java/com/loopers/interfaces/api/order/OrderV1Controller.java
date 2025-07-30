package com.loopers.interfaces.api.order;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.domain.order.OrderItem;
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
        @RequestHeader(
            name = "X-USER-ID",
            required = true
        ) String userId,
        @RequestBody OrderRequest request
    ) {
        List<OrderItem> orderItems = request.items()
            .stream()
            .map(item -> new OrderItem(item.productId(), item.productOptionId(), item.quantity()))
            .toList();
        UserInfo user = userFacade.getUser(userId);
        OrderInfo orderInfo = orderFacade.placeOrder(user.id(), orderItems);
        return ApiResponse.success(OrderResponse.from(orderInfo));
    }
}
