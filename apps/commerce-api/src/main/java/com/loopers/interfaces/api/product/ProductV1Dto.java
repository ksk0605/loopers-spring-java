package com.loopers.interfaces.api.product;

import java.util.List;

import com.loopers.application.brand.BrandInfo;
import com.loopers.application.product.ProductInfo;

public class ProductV1Dto {
    public record ProductResponse(
        Long id,
        String name,
        String description,
        Long price,
        String status,
        BrandInfo brand,
        List<String> imageUrls,
        Long likeCount
    ) {
        public static ProductResponse from(ProductInfo productInfo) {
            return new ProductResponse(
                productInfo.id(),
                productInfo.name(),
                productInfo.description(),
                productInfo.price(),
                productInfo.status(),
                productInfo.brand(),
                productInfo.imagesUrls(),
                productInfo.likeCount()
            );
        }
    }

    public record ProductsResponse(
        List<ProductResponse> products
    ) {
    }
}
