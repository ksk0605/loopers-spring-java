package com.loopers.domain.brand;

import java.util.Optional;

public interface BrandCacheRepository {
    Optional<Brand> getBrand(Long id);

    void setBrand(Long id, Brand brand);

    void invalidateBrand(Long id);
}
