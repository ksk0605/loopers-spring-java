package com.loopers.application.product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final LikeService likeService;

    @Transactional(readOnly = true)
    public ProductResult getProduct(Long productId) {
        Product product = productService.get(productId);
        Brand brand = brandService.get(product.getBrandId());
        var likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductResult.of(product, brand, likeCount);
    }

    @Transactional(readOnly = true)
    public ProductResults getProducts(ProductCommand.Search command) {
        var products = productService.getAll(command);
        var brands = brandService.getAll(products.getContent().stream()
            .map(product -> product.getBrandId())
            .toList());

        var pageInfo = new PageInfo(
            products.getNumber(),
            products.getSize(),
            products.getTotalPages(),
            products.getTotalElements(),
            products.hasNext()
        );

        var productResults = products.getContent().stream()
            .map(product -> {
                var brandInfo = brands.stream()
                    .filter(brand -> brand.getId().equals(product.getBrandId()))
                    .findFirst()
                    .get();
                var likeCount = likeService.count(product.getId(), LikeTargetType.PRODUCT);
                return ProductResult.of(product, brandInfo, likeCount);
            })
            .toList();
        return new ProductResults(productResults, pageInfo);
    }
}
