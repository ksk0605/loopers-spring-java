package com.loopers.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductInfo(
    Long id,
    String name,
    String description,
    BigDecimal price,
    ProductStatus status,
    Long categoryId,
    Long brandId,
    LocalDateTime saleStartDate,
    LocalDateTime saleEndDate,
    List<ProductImageInfo> images,
    List<ProductOptionInfo> options
) {
    public static ProductInfo from(Product product) {
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStatus(),
            product.getCategoryId(),
            product.getBrandId(),
            product.getSaleStartDate(),
            product.getSaleEndDate(),
            product.getImages().stream().map(ProductImageInfo::from).toList(),
            product.getOptions().stream().map(ProductOptionInfo::from).toList()
        );
    }
}
