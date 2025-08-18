package com.loopers.interfaces.api.like;

import java.util.List;

import com.loopers.application.brand.BrandResult;
import com.loopers.application.like.LikeResult;
import com.loopers.application.product.ProductResult;

public class LikeV1Dto {
    public record LikeResponse(Long userId, Long targetId, String targetType) {
        public static LikeResponse from(LikeResult result) {
            return new LikeResponse(
                result.userId(),
                result.targetId(),
                result.targetType().name()
            );
        }
    }

    public record LikedProductsResponse(
        List<LikedProductResponse> products
    ) {
        public static LikedProductsResponse from(List<ProductResult> products) {
            return new LikedProductsResponse(products.stream()
                .map(LikedProductResponse::from)
                .toList());
        }
    }

    public record LikedProductResponse(
        Long id,
        String name,
        Long price,
        String status,
        BrandResponse brand,
        Long likeCount
    ) {
        public static LikedProductResponse from(ProductResult product) {
            return new LikedProductResponse(
                product.id(),
                product.name(),
                product.price(),
                product.status(),
                BrandResponse.from(product.brand()),
                product.likeCount()
            );
        }

        public record BrandResponse(
            Long id,
            String name,
            String description,
            String logoUrl
        ) {
            public static BrandResponse from(BrandResult brand) {
                return new BrandResponse(
                    brand.id(),
                    brand.name(),
                    brand.description(),
                    brand.logoUrl()
                );
            }
        }
    }
}
