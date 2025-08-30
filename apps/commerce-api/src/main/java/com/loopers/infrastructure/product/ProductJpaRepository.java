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
        WHERE p.status = :status
        ORDER BY p.saleStartDate DESC
        """)
    Page<Product> findByStatusOrderByLatest(@Param("status") ProductStatus status, Pageable pageable);

    @Query("""
        SELECT p FROM Product p
        WHERE p.status = :status
        ORDER BY p.price ASC
        """)
    Page<Product> findByStatusOrderByPrice(@Param("status") ProductStatus status, Pageable pageable);

    @Query("""
        SELECT p FROM UserSignal us
        JOIN Product p ON p.id = us.targetId
        WHERE us.targetType = 'PRODUCT' AND p.status = :status
        ORDER BY us.likeCount DESC
        """)
    Page<Product> findByStatusOrderByLikes(@Param("status") ProductStatus status, Pageable pageable);
}
