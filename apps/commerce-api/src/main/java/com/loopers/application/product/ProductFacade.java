package com.loopers.application.product;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.inventory.InventoryService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.like.TargetLikeCount;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductOptionInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final BrandService brandService;
    private final LikeService likeService;

    @Transactional(readOnly = true)
    public ProductDetailResult getProduct(Long productId) {
        ProductInfo product = productService.getInfo(productId);
        List<Long> optionIds = product.getOptions().stream()
            .map(ProductOptionInfo::getId)
            .toList();
        Map<Long, Integer> stockQuantities = inventoryService.getStockQuantities(optionIds);
        Brand brand = brandService.get(product.getBrandId());
        var likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductDetailResult.of(product, brand, likeCount, stockQuantities);
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
