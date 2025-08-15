package com.loopers.application.product;

import java.util.List;
import java.util.Map;

import com.loopers.application.brand.BrandResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductOption;

public record ProductDetailResult(
    Long id,
    String name,
    String description,
    Long price,
    String status,
    BrandResult brand,
    List<String> imagesUrls,
    Long likeCount,
    List<ProductOptionResult> options
) {

    public static ProductDetailResult of(Product product, Brand brand, Long likeCount, List<ProductOption> options,
            Map<Long, Integer> stockQuantities) {
        return new ProductDetailResult(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice().longValue(),
            product.getStatus().name(),
            BrandResult.from(brand),
            product.getImages().stream().map(productImage -> productImage.getImageUrl()).toList(),
            likeCount,
            options.stream()
                .map(option -> ProductOptionResult.of(option, stockQuantities.get(option.getId())))
                .toList()
        );
    }
}
