package com.loopers.domain.product;

public record ProductImageInfo(
    String imageUrl,
    boolean isMain,
    int sortOrder
) {
    public static ProductImageInfo from(ProductImage productImage) {
        return new ProductImageInfo(productImage.getImageUrl(), productImage.isMain(), productImage.getSortOrder());
    }
}
