package com.loopers.application.product;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.like.TargetLikeCount;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final LikeService likeService;

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#productId", unless = "#result == null")
    public ProductResult getProduct(Long productId) {
        Product product = productService.get(productId);
        Brand brand = brandService.get(product.getBrandId());
        var likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductResult.of(product, brand, likeCount);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#command", unless = "#result == null")
    public ProductResults getProducts(ProductCommand.Search command) {
        var products = productService.getAll(command);
        List<Long> brandIds = products.getContent().stream()
            .map(product -> product.getBrandId())
            .toList();
        var brands = brandService.getAll(brandIds);

        var pageInfo = new PageInfo(
            products.getNumber(),
            products.getSize(),
            products.getTotalPages(),
            products.getTotalElements(),
            products.hasNext());

        List<Long> productIds = products.getContent().stream()
            .map(product -> product.getId())
            .toList();
        List<TargetLikeCount> targetLikeCounts = likeService.getAllByTargetIn(productIds, LikeTargetType.PRODUCT);
        return ProductResults.of(products, brands, targetLikeCounts, pageInfo);
    }
}
