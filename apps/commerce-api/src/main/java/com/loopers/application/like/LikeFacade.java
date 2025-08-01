package com.loopers.application.like;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.LikeService;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeFacade {
    private final UserService userService;
    private final LikeService likeService;
    private final ProductService productService;

    public LikeResult likeProduct(LikeCriteria.LikeProduct criteria) {
        var userInfo = userService.get(criteria.userId());
        var productInfo = productService.get(criteria.productId());
        likeService.like(userInfo.id(), productInfo.id(), LikeTargetType.PRODUCT);
        return new LikeResult(userInfo.id(), productInfo.id(), LikeTargetType.PRODUCT);
    }
}
