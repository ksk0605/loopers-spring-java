package com.loopers.interfaces.api.brand;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandV1ApiController implements BrandV1ApiSpec {

    @GetMapping("/{brandId}")
    @Override
    public ApiResponse<BrandV1Dto.BrandResponse> getBrand(@PathVariable Long brandId) {
        if (brandId != 1L) {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
        return ApiResponse.success(
            new BrandV1Dto.BrandResponse(
                1L,
                "테스트 브랜드",
                "브랜드 설명",
                "https://test.image.url"
            )
        );
    }
}
