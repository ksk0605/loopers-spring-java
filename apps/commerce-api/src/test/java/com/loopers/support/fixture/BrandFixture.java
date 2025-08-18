package com.loopers.support.fixture;

import com.loopers.domain.brand.Brand;

public class BrandFixture {
    private String name = "테스트 브랜드";
    private String description = "이 브랜드는 테스트 브랜드 입니다.";
    private String logoUrl = "logoUrl";

    public static BrandFixture aBrand() {
        return new BrandFixture();
    }

    public BrandFixture name(String name) {
        this.name = name;
        return this;
    }

    public BrandFixture description(String description) {
        this.description = description;
        return this;
    }

    public BrandFixture logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public Brand build() {
        return new Brand(
            name,
            description,
            logoUrl
        );
    }

    private BrandFixture() {
    }
}
