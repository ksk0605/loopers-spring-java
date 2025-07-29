package com.loopers.application.brand;

import org.springframework.stereotype.Component;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BrandFacade {
    private final BrandService brandService;

    public BrandInfo getBrand(Long id) {
        Brand brand = brandService.get(id);
        return BrandInfo.from(brand);
    }
}
