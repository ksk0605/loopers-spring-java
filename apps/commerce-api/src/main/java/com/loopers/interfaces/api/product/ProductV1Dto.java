package com.loopers.interfaces.api.product;

import java.util.List;

import com.loopers.application.common.PageInfo;
import com.loopers.application.product.ProductResult;
import com.loopers.application.product.ProductsInfo;
import com.loopers.domain.brand.BrandInfo;

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
        public static ProductResponse from(ProductResult productResult) {
            return new ProductResponse(
                productResult.id(),
                productResult.name(),
                productResult.description(),
                productResult.price(),
                productResult.status(),
                productResult.brand(),
                productResult.imagesUrls(),
                productResult.likeCount()
            );
        }
    }

    public record ProductsResponse(
        List<ProductResponse> products,
        PageInfo pageInfo
    ) {
        public static ProductsResponse from(ProductsInfo productsInfo) {
            return new ProductsResponse(
                productsInfo.products().stream()
                    .map(ProductResponse::from)
                    .toList(),
                productsInfo.pageInfo()
            );
        }
    }
}
