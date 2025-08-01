package com.loopers.application.product;

import java.util.List;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.product.ProductImage;
import com.loopers.domain.product.ProductImageInfo;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductSummary;

public record ProductResult(
    Long id,
    String name,
    String description,
    long price,
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

    public static ProductResult from(ProductSummary summary) {
        BrandInfo brandInfo = new BrandInfo(
            summary.getBrandId(),
            summary.getBrandName(),
            summary.getBrandDescription(),
            summary.getBrandLogoUrl()
        );
        return new ProductResult(
            summary.getId(),
            summary.getName(),
            summary.getDescription(),
            summary.getPrice().longValue(),
            summary.getStatus().name(),
            brandInfo,
            summary.getImages().stream().map(ProductImage::getImageUrl).toList(),
            summary.getLikeCount()
        );
    }
}
