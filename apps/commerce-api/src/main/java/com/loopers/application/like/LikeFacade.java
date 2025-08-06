package com.loopers.application.like;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.application.product.ProductResult;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LikeFacade {
    private final UserService userService;
    private final LikeService likeService;
    private final ProductService productService;
    private final BrandService brandService;

    @Transactional
    public LikeResult likeProduct(LikeCriteria.LikeProduct criteria) {
        User user = userService.get(criteria.userId());
        Product product = productService.get(criteria.productId());
        likeService.like(user.getId(), product.getId(), LikeTargetType.PRODUCT);
        return new LikeResult(user.getId(), product.getId(), LikeTargetType.PRODUCT);
    }

    @Transactional
    public void unlikeProduct(LikeCriteria.UnlikeProduct criteria) {
        User user = userService.get(criteria.userId());
        Product product = productService.get(criteria.productId());
        likeService.unlike(user.getId(), product.getId(), LikeTargetType.PRODUCT);
    }

    public List<ProductResult> getLikedProducts(LikeCriteria.GetLiked criteria) {
        User user = userService.get(criteria.userId());
        List<Like> likes = likeService.getAll(user.getId(), criteria.targetType());
        List<Long> likeIds = likes.stream()
            .map(like -> like.getTarget().id())
            .toList();
        List<Product> products = productService.getAll(likeIds);
        List<Brand> brands = brandService.getAll(products.stream()
            .map(product -> product.getBrandId())
            .toList());

        return products.stream()
            .map(product -> {
                var brandInfo = brands.stream()
                    .filter(b -> b.getId().equals(product.getBrandId()))
                    .findFirst()
                    .get();
                var likeCount = likeService.count(product.getId(), LikeTargetType.PRODUCT);
                return ProductResult.of(product, brandInfo, likeCount);
            })
            .toList();
    }
}
