package com.loopers.domain.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface ProductRepository {
    Optional<Product> find(Long id);

    List<Product> findAll(List<Long> ids);

    Page<Product> findAll(ProductCommand.Search command);
}
