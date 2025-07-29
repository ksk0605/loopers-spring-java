package com.loopers.application.product;

import java.util.List;

import com.loopers.application.brand.BrandInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductImage;

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
    public static ProductInfo of(Product product, Brand brand, Long likeCount) {
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice().longValue(),
            product.getStatus().name(),
            BrandInfo.from(brand),
            product.getImages().stream().map(ProductImage::getImageUrl).toList(),
            likeCount
        );
    }
}
