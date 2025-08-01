package com.loopers.infrastructure.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN LikeSummary ls ON ls.target.id = p.id AND ls.target.type = 'PRODUCT'
        WHERE (:status IS NULL OR p.status = :status)
        ORDER BY
        CASE WHEN :sortBy = 'LIKES_DESC' THEN ls.likeCount END DESC NULLS LAST,
        CASE WHEN :sortBy = 'LATEST' THEN p.saleStartDate END DESC,
        CASE WHEN :sortBy = 'PRICE_ASC' THEN p.price END ASC
        """)
    Page<Product> findAll(
        @Param("status") ProductStatus status,
        @Param("sortBy") String sortBy,
        Pageable pageable);
}
