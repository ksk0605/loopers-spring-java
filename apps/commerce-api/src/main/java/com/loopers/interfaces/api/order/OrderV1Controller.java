package com.loopers.interfaces.api.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderRequest;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderResponse;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderV1Controller implements OrderV1ApiSpec {

    @PostMapping
    @Override
    public ApiResponse<OrderResponse> createOrder(
        @RequestBody OrderRequest request
    ) {
        return ApiResponse.success(
            new OrderResponse(
                1L, 
                List.of(
                    new OrderV1Dto.OrderItemResponse(1L, 1L, 1)
                ), 
                1L, 
                "PAYMENT_COMPLETED", 
                "POINT",
                "COMPLETED", 
                LocalDateTime.now(), 
                10000L
                )
            );
    }
}
