package com.loopers.application.brand;

import com.loopers.domain.brand.Brand;

public record BrandResult(
    Long id, String name, String description, String logoUrl
) {
    public static BrandResult from(Brand brand) {
        return new BrandResult(
            brand.getId(),
            brand.getName(),
            brand.getDescription(),
            brand.getLogoUrl()
        );
    }
}
