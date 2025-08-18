package com.loopers.interfaces.api.product;

import java.util.List;

import com.loopers.application.brand.BrandResult;
import com.loopers.application.common.PageInfo;
import com.loopers.application.product.ProductDetailResult;
import com.loopers.application.product.ProductOptionResult;
import com.loopers.application.product.ProductResult;
import com.loopers.application.product.ProductResults;

public class ProductV1Dto {

    public record ProductDetailResponse(
        Long id,
        String name,
        String description,
        Long price,
        String status,
        Brand brand,
        List<String> imageUrls,
        Long likeCount,
        List<ProductOptionResponse> options
    ) {
        public static ProductDetailResponse from(ProductDetailResult productResult) {
            return new ProductDetailResponse(
                productResult.id(),
                productResult.name(),
                productResult.description(),
                productResult.price(),
                productResult.status(),
                Brand.from(productResult.brand()),
                productResult.imagesUrls(),
                productResult.likeCount(),
                productResult.options().stream()
                    .map(ProductOptionResponse::from)
                    .toList()
            );
        }
    }

    public record ProductResponse(
        Long id,
        String name,
        String description,
        Long price,
        String status,
        Brand brand,
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
                Brand.from(productResult.brand()),
                productResult.imagesUrls(),
                productResult.likeCount()
            );
        }
    }

    public record ProductsResponse(
        List<ProductResponse> products,
        PageInfo pageInfo
    ) {
        public static ProductsResponse from(ProductResults result) {
            return new ProductsResponse(
                result.products().stream()
                    .map(ProductResponse::from)
                    .toList(),
                result.pageInfo()
            );
        }
    }

    public record Brand(
        Long id,
        String name,
        String description,
        String logoUrl
    ) {
        public static Brand from(BrandResult brandResult) {
            return new Brand(
                brandResult.id(),
                brandResult.name(),
                brandResult.description(),
                brandResult.logoUrl()
            );
        }
    }

    public record ProductOptionResponse(
        Long id,
        String optionType,
        String optionValue,
        Long additionalPrice,
        Integer stockQuantity
    ) {
        public static ProductOptionResponse from(ProductOptionResult productOptionResult) {
            return new ProductOptionResponse(
                productOptionResult.id(),
                productOptionResult.optionType(),
                productOptionResult.optionValue(),
                productOptionResult.additionalPrice(),
                productOptionResult.stockQuantity()
            );
        }
    }
}
