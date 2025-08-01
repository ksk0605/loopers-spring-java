package com.loopers.application.product;

import java.util.List;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.product.ProductImageInfo;
import com.loopers.domain.product.ProductInfo;

public record ProductResult(
    Long id,
    String name,
    String description,
    Long price,
    String status,
    BrandInfo brand,
    List<String> imagesUrls,
    Long likeCount
) {
    public static ProductResult of(ProductInfo productInfo, BrandInfo brand, Long likeCount) {
        return new ProductResult(
            productInfo.id(),
            productInfo.name(),
            productInfo.description(),
            productInfo.price().longValue(),
            productInfo.status().name(),
            brand,
            productInfo.images().stream().map(ProductImageInfo::imageUrl).toList(),
            likeCount
        );
    }
}
