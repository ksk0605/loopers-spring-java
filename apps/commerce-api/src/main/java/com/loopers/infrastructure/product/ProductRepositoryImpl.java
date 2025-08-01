package com.loopers.infrastructure.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> find(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<Product> findAll(List<Long> ids) {
        return productJpaRepository.findAllById(ids);
    }
}
