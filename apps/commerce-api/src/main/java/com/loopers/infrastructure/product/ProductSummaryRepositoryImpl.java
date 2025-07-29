package com.loopers.infrastructure.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.loopers.domain.product.ProductImage;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.product.ProductSummary;
import com.loopers.domain.product.ProductSummaryRepository;
import com.loopers.domain.product.SummarySearchCondition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductSummaryRepositoryImpl implements ProductSummaryRepository {
    private final EntityManager entityManager;

    @Override
    public Page<ProductSummary> findAll(SummarySearchCondition condition) {
        String jpql = """
            SELECT
                p.id,
                p.name,
                p.description,
                p.price,
                p.status,
                p.brandId,
                b.name as brandName,
                b.description as brandDescription,
                b.logoUrl as brandLogoUrl,
                p.saleStartDate,
                p.saleEndDate,
                (SELECT COUNT(l) FROM Like l WHERE l.target.id = p.id AND l.target.type = 'PRODUCT') as likeCount
            FROM Product p
            LEFT JOIN Brand b ON p.brandId = b.id
            WHERE p.status = :status
            ORDER BY
                CASE WHEN :sortBy = 'LIKES' THEN (SELECT COUNT(l) FROM Like l WHERE l.target.id = p.id AND l.target.type = 'PRODUCT') END DESC,
                CASE WHEN :sortBy = 'LATEST' THEN p.saleStartDate END DESC,
                CASE WHEN :sortBy = 'PRICE' THEN p.price END ASC
            """;

        Query query = entityManager.createQuery(jpql);
        query.setParameter("status", condition.getStatus());
        query.setParameter("sortBy", condition.getSortBy().name());
        query.setFirstResult(condition.getOffset());
        query.setMaxResults(condition.getLimit());

        List<Object[]> results = query.getResultList();

        List<ProductSummary> productSummaries = results.stream()
            .map(this::convert)
            .toList();

        Long total = getTotalCount(condition);

        getProductImages(productSummaries);

        return new PageImpl<>(
            productSummaries,
            PageRequest.of(condition.getPage(), condition.getSize()),
            total);
    }

    private ProductSummary convert(Object[] result) {
        return ProductSummary.builder()
            .id((Long)result[0])
            .name((String)result[1])
            .description((String)result[2])
            .price((BigDecimal)result[3])
            .status((ProductStatus)result[4])
            .brandId((Long)result[5])
            .brandName((String)result[6])
            .brandDescription((String)result[7])
            .brandLogoUrl((String)result[8])
            .saleStartDate((LocalDateTime)result[9])
            .saleEndDate((LocalDateTime)result[10])
            .likeCount((Long)result[11])
            .build();
    }

    private Long getTotalCount(SummarySearchCondition condition) {
        String countJpql = "SELECT COUNT(p) FROM Product p WHERE p.status = :status";
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        countQuery.setParameter("status", condition.getStatus());
        return countQuery.getSingleResult();
    }

    private void getProductImages(List<ProductSummary> productSummaries) {
        if (productSummaries.isEmpty()) {
            return;
        }

        List<Long> productIds = productSummaries.stream()
            .map(ProductSummary::getId)
            .toList();

        String sql = """
            SELECT
                pi.id,
                pi.image_url,
                pi.is_main,
                pi.sort_order,
                pi.product_id
            FROM product_image pi
            WHERE pi.product_id IN (:productIds)
            ORDER BY pi.product_id, pi.sort_order
            """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("productIds", productIds);

        List<Object[]> allImages = query.getResultList();

        Map<Long, List<ProductImage>> imageMap = new HashMap<>();

        for (Object[] imageResult : allImages) {
            Long productId = ((Number)imageResult[4]).longValue();
            String imageUrl = (String)imageResult[1];
            Boolean isMain = (Boolean)imageResult[2];
            int sortOrder = ((Number)imageResult[3]).intValue();

            ProductImage productImage = new ProductImage(imageUrl, isMain, sortOrder);

            imageMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(productImage);
        }

        for (ProductSummary productSummary : productSummaries) {
            List<ProductImage> images = imageMap.getOrDefault(productSummary.getId(), new ArrayList<>());
            productSummary.setImages(images);
        }
    }
}
