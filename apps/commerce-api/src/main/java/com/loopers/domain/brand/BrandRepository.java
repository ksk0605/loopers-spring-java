package com.loopers.domain.brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> find(Long id);

    List<Brand> findAll(List<Long> ids);
}
