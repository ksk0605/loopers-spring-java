package com.loopers.interfaces.api.like;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.like.LikeCriteria;
import com.loopers.application.like.LikeFacade;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.interfaces.api.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeV1ApiController implements LikeV1ApiSpec {
    private final LikeFacade likeFacade;

    @PostMapping("/products/{productId}")
    @Override
    public ApiResponse<LikeV1Dto.LikeResponse> likeProduct(
        @RequestHeader(name = "X-USER-ID") String userId,
        @PathVariable Long productId) {
        var criteria = new LikeCriteria.LikeProduct(userId, productId, LikeTargetType.PRODUCT);
        var result = likeFacade.likeProduct(criteria);
        return ApiResponse.success(LikeV1Dto.LikeResponse.from(result));
    }
}
