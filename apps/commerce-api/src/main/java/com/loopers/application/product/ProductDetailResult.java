package com.loopers.application.product;

import java.util.List;
import java.util.Map;

import com.loopers.application.brand.BrandResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.ProductInfo;

public record ProductDetailResult(
        Long id,
        String name,
        String description,
        Long price,
        String status,
        BrandResult brand,
        List<String> imagesUrls,
        Long likeCount,
        List<ProductOptionResult> options) {

    public static ProductDetailResult of(ProductInfo product, Brand brand, Long likeCount,
            Map<Long, Integer> stockQuantities) {
        return new ProductDetailResult(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStatus(),
                BrandResult.from(brand),
                product.getImageUrls(),
                likeCount,
                product.getOptions().stream()
                        .map(option -> ProductOptionResult.of(option, stockQuantities.get(option.getId())))
                        .toList());
    }
}
