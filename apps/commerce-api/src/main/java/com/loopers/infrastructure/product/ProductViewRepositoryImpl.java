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
import com.loopers.domain.product.ProductSearchCondition;
import com.loopers.domain.product.ProductStatus;
import com.loopers.domain.product.ProductView;
import com.loopers.domain.product.ProductViewRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductViewRepositoryImpl implements ProductViewRepository {
    private final EntityManager entityManager;

    @Override
    public Page<ProductView> findProducts(ProductSearchCondition condition) {
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

        List<ProductView> productViews = results.stream()
            .map(this::convertToProductView)
            .toList();

        Long total = getTotalCount(condition);

        getProductImages(productViews);

        return new PageImpl<>(
            productViews,
            PageRequest.of(condition.getPage(), condition.getSize()),
            total);
    }

    private ProductView convertToProductView(Object[] result) {
        ProductView productView = new ProductView();
        productView.setId((Long)result[0]);
        productView.setName((String)result[1]);
        if (result[2] != null) {
            productView.setDescription((String)result[2]);
        }
        productView.setPrice((BigDecimal)result[3]);
        productView.setStatus(((ProductStatus)result[4]));

        ProductView.Brand brandInfo = new ProductView.Brand();
        brandInfo.setId((Long)result[5]);
        if (result[6] != null) {
            brandInfo.setDescription((String)result[6]);
        }
        if (result[7] != null) {
            brandInfo.setLogoUrl((String)result[7]);
        }
        productView.setBrand(brandInfo);

        productView.setSaleStartDate((LocalDateTime)result[9]);
        if (result[10] != null) {
            productView.setSaleEndDate((LocalDateTime)result[10]);
        }
        productView.setLikeCount((Long)result[11]);
        return productView;
    }

    private Long getTotalCount(ProductSearchCondition condition) {
        String countJpql = "SELECT COUNT(p) FROM Product p WHERE p.status = :status";
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        countQuery.setParameter("status", condition.getStatus());
        return countQuery.getSingleResult();
    }

    private void getProductImages(List<ProductView> productViews) {
        if (productViews.isEmpty()) {
            return;
        }

        List<Long> productIds = productViews.stream()
            .map(ProductView::getId)
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
            Integer sortOrder = ((Number)imageResult[3]).intValue();

            ProductImage productImage = new ProductImage(imageUrl, isMain, sortOrder);

            imageMap.computeIfAbsent(productId, k -> new ArrayList<>()).add(productImage);
        }

        for (ProductView productView : productViews) {
            List<ProductImage> images = imageMap.getOrDefault(productView.getId(), new ArrayList<>());
            productView.setImages(images);
        }
    }
}
