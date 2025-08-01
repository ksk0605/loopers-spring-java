package com.loopers.application.like;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.application.product.ProductResult;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeFacade {
    private final UserService userService;
    private final LikeService likeService;
    private final ProductService productService;
    private final BrandService brandService;

    public LikeResult likeProduct(LikeCriteria.LikeProduct criteria) {
        var userInfo = userService.get(criteria.userId());
        var productInfo = productService.get(criteria.productId());
        likeService.like(userInfo.id(), productInfo.id(), LikeTargetType.PRODUCT);
        return new LikeResult(userInfo.id(), productInfo.id(), LikeTargetType.PRODUCT);
    }

    public void unlikeProduct(LikeCriteria.UnlikeProduct criteria) {
        var userInfo = userService.get(criteria.userId());
        var productInfo = productService.get(criteria.productId());
        likeService.unlike(userInfo.id(), productInfo.id(), LikeTargetType.PRODUCT);
    }

    public List<ProductResult> getLikedProducts(Long userId, LikeTargetType targetType) {
        var likeInfos = likeService.getAll(userId, targetType);
        var productInfos = productService.getAll(likeInfos.stream()
            .map(LikeInfo::targetId)
            .toList());
        var brandInfos = brandService.getAll(productInfos.stream()
            .map(ProductInfo::brandId)
            .toList());

        return productInfos.stream()
            .map(productInfo -> {
                var brandInfo = brandInfos.stream()
                    .filter(b -> b.id().equals(productInfo.brandId()))
                    .findFirst()
                    .get();
                var likeCount = likeService.count(productInfo.id(), LikeTargetType.PRODUCT);
                return ProductResult.of(productInfo, brandInfo, likeCount);
            })
            .toList();
    }
}
