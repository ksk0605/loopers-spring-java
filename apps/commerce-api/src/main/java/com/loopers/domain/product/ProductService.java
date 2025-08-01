package com.loopers.domain.product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductInfo get(Long id) {
        Product product = productRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
        return ProductInfo.from(product);
    }
}
