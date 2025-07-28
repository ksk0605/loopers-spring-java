package com.loopers.infrastructure.brand;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Optional<Brand> find(Long id) {
        return brandJpaRepository.findById(id);
    }
}
