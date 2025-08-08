package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.BrandResult;

public class BrandV1Dto {
    public record BrandResponse(Long id, String name, String description, String logoUrl) {
        public static BrandResponse from(BrandResult brandInfo) {
            return new BrandResponse(
                brandInfo.id(),
                brandInfo.name(),
                brandInfo.description(),
                brandInfo.logoUrl()
            );
        }
    }
}
