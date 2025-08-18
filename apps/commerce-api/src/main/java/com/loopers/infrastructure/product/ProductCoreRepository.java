package com.loopers.infrastructure.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductCommand.Search;
import com.loopers.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCoreRepository implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> find(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public List<Product> findAll(List<Long> ids) {
        return productJpaRepository.findAllById(ids);
    }

    @Override
    public Page<Product> findAll(Search command) {
        PageRequest pageRequest = PageRequest.of(command.page(), command.size());
        if (command.sortBy().isLatest()) {
            return productJpaRepository.findByStatusOrderByLatest(command.status(), pageRequest);
        }
        if (command.sortBy().isPriceAsc()) {
            return productJpaRepository.findByStatusOrderByPrice(command.status(), pageRequest);
        }
        return productJpaRepository.findByStatusOrderByLikes(command.status(), pageRequest);
    }
}
