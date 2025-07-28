package com.loopers.application.product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final LikeService likeService;

    @Transactional(readOnly = true)
    public ProductInfo getProduct(Long productId) {
        Product product = productService.get(productId);
        Brand brand = brandService.get(product.getBrandId());
        Long likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductInfo.of(product, brand, likeCount);
    }
}
