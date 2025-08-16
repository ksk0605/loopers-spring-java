package com.loopers.domain.brand;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandCacheRepository brandCacheRepository;

    public Brand get(Long id) {
        Optional<Brand> cachedBrand = brandCacheRepository.getBrand(id);
        if (cachedBrand.isPresent()) {
            return cachedBrand.get();
        }
        Brand brand = brandRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + id + "] 브랜드를 찾을 수 없습니다."));
        brandCacheRepository.setBrand(id, brand);
        return brand;
    }

    @Transactional(readOnly = true)
    public List<Brand> getAll(List<Long> ids) {
        return brandRepository.findAll(ids);
    }
}
