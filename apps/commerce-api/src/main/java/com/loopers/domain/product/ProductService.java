package com.loopers.domain.product;

import java.util.List;

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

    @Transactional(readOnly = true)
    public ProductInfo get(Long id) {
        Product product = productRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: " + id));
        return ProductInfo.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductInfo> getAll(List<Long> ids) {
        return productRepository.findAll(ids)
            .stream()
            .map(ProductInfo::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductInfo> getAll(ProductCommand.Search command) {
        Page<Product> products = productRepository.findAll(command);
        return products.map(ProductInfo::from);
    }
}
