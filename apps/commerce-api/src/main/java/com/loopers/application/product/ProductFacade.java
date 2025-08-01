package com.loopers.application.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.common.PageInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.product.ProductSummary;
import com.loopers.domain.product.ProductSummaryService;
import com.loopers.domain.product.SortBy;
import com.loopers.domain.product.SummarySearchCondition;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;
    private final BrandService brandService;
    private final LikeService likeService;
    private final ProductSummaryService productSummaryService;

    @Transactional(readOnly = true)
    public ProductResult getProduct(Long productId) {
        var productInfo = productService.get(productId);
        var brandInfo = brandService.get(productInfo.brandId());
        var likeCount = likeService.count(productId, LikeTargetType.PRODUCT);
        return ProductResult.of(productInfo, brandInfo, likeCount);
    }

    @Transactional(readOnly = true)
    public ProductsInfo getProducts(String sortBy, int page, int size) {
        Page<ProductSummary> summaries = productSummaryService.summaries(
            new SummarySearchCondition(SortBy.valueOf(sortBy), page, size, ProductStatus.ON_SALE));
        PageInfo pageInfo = new PageInfo(
            summaries.getNumber(),
            summaries.getSize(),
            summaries.getTotalPages(),
            summaries.getTotalElements(),
            summaries.hasNext()
        );
        return ProductsInfo.of(summaries.getContent(), pageInfo);
    }
}
