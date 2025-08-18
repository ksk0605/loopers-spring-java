package com.loopers.application.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.TargetLikeCount;
import com.loopers.domain.product.Product;

public record ProductResults(
    List<ProductResult> products,
    PageInfo pageInfo
) {
    public static ProductResults of(Page<Product> products, List<Brand> brands, List<TargetLikeCount> targetLikeCounts,
        PageInfo pageInfo) {
        var productResults = products.getContent().stream()
            .map(product -> {
                var brand = getBrand(brands, product);
                var likeCount = getLikeCount(targetLikeCounts, product);
                return ProductResult.of(product, brand, likeCount);
            })
            .toList();

        return new ProductResults(productResults, pageInfo);
    }

    private static Brand getBrand(List<Brand> brands, Product product) {
        return brands.stream()
            .filter(brand -> brand.getId().equals(product.getBrandId()))
            .findFirst()
            .get();
    }

    private static Long getLikeCount(List<TargetLikeCount> targetLikeCounts, Product product) {
        return targetLikeCounts.stream()
            .filter(tlc -> tlc.getTarget().id().equals(product.getId()))
            .map(TargetLikeCount::getLikeCount)
            .findFirst()
            .orElse(0L);
    }
}
