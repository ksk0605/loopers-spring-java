package com.loopers.application.product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
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
        var productInfo = productService.get(productId);
        var brandInfo = brandService.get(productInfo.brandId());
        var likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductResult.of(productInfo, brandInfo, likeCount);
    }

    @Transactional(readOnly = true)
    public ProductResults getProducts(ProductCommand.Search command) {
        var productInfos = productService.getAll(command);
        var brands = brandService.getAll(productInfos.getContent().stream()
            .map(ProductInfo::brandId)
            .toList());

        var pageInfo = new PageInfo(
            productInfos.getNumber(),
            productInfos.getSize(),
            productInfos.getTotalPages(),
            productInfos.getTotalElements(),
            productInfos.hasNext()
        );

        var productResults = productInfos.getContent().stream()
            .map(productInfo -> {
                var brandInfo = brands.stream()
                    .filter(brand -> brand.id().equals(productInfo.brandId()))
                    .findFirst()
                    .get();
                var likeCount = likeService.count(productInfo.id(), LikeTargetType.PRODUCT);
                return ProductResult.of(productInfo, brandInfo, likeCount);
            })
            .toList();
        return new ProductResults(productResults, pageInfo);
    }
}
