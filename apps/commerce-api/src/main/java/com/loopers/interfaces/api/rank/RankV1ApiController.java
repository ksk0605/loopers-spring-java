package com.loopers.interfaces.api.rank;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductResults;
import com.loopers.domain.rank.RankCommand;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductV1Dto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
public class RankV1ApiController implements RankV1ApiSpec {
    private final ProductFacade productFacade;

    @Override
    public ApiResponse<ProductV1Dto.ProductsResponse> getRankingProducts(
        @RequestParam String periodKey,
        @RequestParam String periodType,
        @RequestParam Integer page,
        @RequestParam Integer size) {
        RankCommand.GetV2 command = RankCommand.GetV2.of(periodKey, periodType, page, size);
        ProductResults result = productFacade.getRanking(command);
        ProductV1Dto.ProductsResponse response = ProductV1Dto.ProductsResponse.from(result);
        return ApiResponse.success(response);
    }
}
