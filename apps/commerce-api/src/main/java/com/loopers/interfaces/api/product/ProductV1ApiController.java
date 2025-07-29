package com.loopers.interfaces.api.product;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.brand.BrandInfo;
import com.loopers.interfaces.api.ApiResponse;

@RestController
@RequestMapping("/api/v1/products")
public class ProductV1ApiController implements ProductV1ApiSpec {
    @GetMapping("/{productId}")
    @Override
    public ApiResponse<ProductV1Dto.ProductResponse> getProduct(
        @PathVariable Long productId
    ) {
        ProductV1Dto.ProductResponse productResponse = new ProductV1Dto.ProductResponse
            (
                1L,
                "테스트 상품",
                "테스트 설명",
                BigDecimal.ONE,
                "SALE",
                new BrandInfo(
                    1L,
                    "brand name",
                    null,
                    null
                ),
                "CATEGORY",
                10,
                new ArrayList<>()
            );
        return ApiResponse.success(productResponse);
    }
}
