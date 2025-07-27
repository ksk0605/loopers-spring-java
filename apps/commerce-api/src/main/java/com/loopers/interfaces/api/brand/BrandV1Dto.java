package com.loopers.interfaces.api.brand;

public class BrandV1Dto {
    public record BrandResponse(Long id, String name, String description, String logoUrl) {
    }
}
