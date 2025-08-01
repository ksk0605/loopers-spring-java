package com.loopers.domain.brand;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public BrandInfo get(Long id) {
        Brand brand = brandRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + id + "] 브랜드를 찾을 수 없습니다."));
        return BrandInfo.from(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandInfo> getAll(List<Long> ids) {
        return brandRepository.findAll(ids)
            .stream()
            .map(BrandInfo::from)
            .toList();
    }
}
