package com.loopers.interfaces.api.rank;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    @Override
    public ApiResponse<ProductV1Dto.ProductsResponse> getDailyRankingProducts(
        @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date,
        @RequestParam Integer page,
        @RequestParam Integer size) {
        RankCommand.Get command = new RankCommand.Get(date, size, page);
        ProductResults result = productFacade.getDailyRanking(command);
        ProductV1Dto.ProductsResponse response = ProductV1Dto.ProductsResponse.from(result);
        return ApiResponse.success(response);
    }
}
