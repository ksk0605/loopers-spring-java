package com.loopers.application.product;

import java.util.List;

import com.loopers.application.brand.BrandResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;

public record ProductResult(
    Long id,
    String name,
    String description,
    Long price,
    String status,
    BrandResult brand,
    List<String> imagesUrls,
    Long likeCount
) {
    public static ProductResult of(Product product, Brand brand, Long likeCount) {
        return new ProductResult(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice().longValue(),
            product.getStatus().name(),
            BrandResult.from(brand),
            product.getImages().stream().map(productImage -> productImage.getImageUrl()).toList(),
            likeCount
        );
    }
}
