package com.loopers.application.product;

import java.util.List;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductImage;
import com.loopers.domain.product.ProductSummary;

public record ProductInfo(
    Long id,
    String name,
    String description,
    long price,
    String status,
    BrandInfo brand,
    List<String> imagesUrls,
    Long likeCount
) {
    public static ProductInfo of(Product product, BrandInfo brand, Long likeCount) {
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice().longValue(),
            product.getStatus().name(),
            brand,
            product.getImages().stream().map(ProductImage::getImageUrl).toList(),
            likeCount
        );
    }

    public static ProductInfo from(ProductSummary summary) {
        BrandInfo brandInfo = new BrandInfo(
            summary.getBrandId(),
            summary.getBrandName(),
            summary.getBrandDescription(),
            summary.getBrandLogoUrl()
        );
        return new ProductInfo(
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
