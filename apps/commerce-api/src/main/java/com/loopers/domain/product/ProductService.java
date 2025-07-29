package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductViewRepository productViewRepository;

    public Product get(Long id) {
        return productRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<ProductView> list(ProductSearchCondition condition) {
        return productViewRepository.findProducts(condition);
    }
}
