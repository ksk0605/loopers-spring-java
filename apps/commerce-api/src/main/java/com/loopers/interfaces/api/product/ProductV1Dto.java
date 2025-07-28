package com.loopers.interfaces.api.product;

import java.math.BigDecimal;
import java.util.List;

import com.loopers.application.brand.BrandInfo;

public class ProductV1Dto {
    public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String status,
        BrandInfo brand,
        String category,
        Integer stockQuantity,
        List<String> images
    ) {
    }

    public record ProductsResponse(
        List<ProductResponse> products
    ) {
    }
}
