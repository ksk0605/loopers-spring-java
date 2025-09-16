package com.loopers.domain.analytics;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "product_metrics",
    uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "period_type", "period_key", "version"})
)
public class ProductMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 16)
    private PeriodType periodType;

    @Column(name = "period_key", nullable = false, length = 32)
    private String periodKey;

    @Column(name = "version", nullable = false, length = 32)
    private String version;

    @Column(name = "likes", nullable = false)
    private Long likes;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "order_count", nullable = false)
    private Long orderCount;

    

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    protected ProductMetrics() {
    }

    public ProductMetrics(
        Long productId,
        PeriodType periodType,
        String periodKey,
        String version,
        Long likes,
        Long views,
        Long orderCount,
        LocalDateTime generatedAt
    ) {
        this.productId = productId;
        this.periodType = periodType;
        this.periodKey = periodKey;
        this.version = version;
        this.likes = likes != null ? likes : 0L;
        this.views = views != null ? views : 0L;
        this.orderCount = orderCount != null ? orderCount : 0L;
        this.generatedAt = generatedAt != null ? generatedAt : LocalDateTime.now();
    }

    public void updateValues(Long likes, Long views, Long orderCount, LocalDateTime generatedAt) {
        if (likes != null)
            this.likes = likes;
        if (views != null)
            this.views = views;
        if (orderCount != null)
            this.orderCount = orderCount;
        if (generatedAt != null)
            this.generatedAt = generatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public String getVersion() {
        return version;
    }

    public Long getLikes() {
        return likes;
    }

    public Long getViews() {
        return views;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
}
