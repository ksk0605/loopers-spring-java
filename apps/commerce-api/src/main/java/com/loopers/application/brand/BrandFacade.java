package com.loopers.application.brand;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class BrandFacade {
    private final BrandService brandService;

    public BrandResult getBrand(Long brandId) {
        Brand brand = brandService.get(brandId);
        return BrandResult.from(brand);
    }
}
