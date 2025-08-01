package com.loopers.interfaces.api.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductResult;
import com.loopers.application.product.ProductsInfo;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductV1ApiController implements ProductV1ApiSpec {
    private final ProductFacade productFacade;

    @GetMapping("/{productId}")
    @Override
    public ApiResponse<ProductV1Dto.ProductResponse> getProduct(
        @PathVariable Long productId
    ) {
        ProductResult productResult = productFacade.getProduct(productId);
        var response = ProductV1Dto.ProductResponse.from(productResult);
        return ApiResponse.success(response);
    }

    @GetMapping
    @Override
    public ApiResponse<ProductV1Dto.ProductsResponse> getProducts(
        @RequestParam String sortBy,
        @RequestParam Integer page,
        @RequestParam Integer size
    ) {
        ProductsInfo productsInfo = productFacade.getProducts(sortBy, page, size);
        ProductV1Dto.ProductsResponse response = ProductV1Dto.ProductsResponse.from(productsInfo);
        return ApiResponse.success(response);
    }
}
