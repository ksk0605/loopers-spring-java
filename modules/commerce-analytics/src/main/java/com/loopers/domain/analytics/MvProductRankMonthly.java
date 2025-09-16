package com.loopers.domain.analytics;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "mv_product_rank_monthly",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"period_key", "version", "rank"}),
        @UniqueConstraint(columnNames = {"period_key", "version", "product_id"})
    },
    indexes = {
        @Index(name = "idx_monthly_period_rank", columnList = "period_key, version, rank"),
        @Index(name = "idx_monthly_period_product", columnList = "period_key, version, product_id")
    }
)
public class MvProductRankMonthly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "period_key", nullable = false, length = 16)
    private String periodKey;

    @Column(name = "version", nullable = false, length = 16)
    private String version;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "likes", nullable = false)
    private Long likes;

    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "order_count", nullable = false)
    private Long orderCount;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    protected MvProductRankMonthly() {
    }

    public MvProductRankMonthly(String periodKey, String version, Integer rank, Long productId) {
        this.periodKey = periodKey;
        this.version = version;
        this.rank = rank;
        this.productId = productId;
        this.score = 0.0d;
        this.likes = 0L;
        this.views = 0L;
        this.orderCount = 0L;
        this.generatedAt = LocalDateTime.now();
    }

    public MvProductRankMonthly(String periodKey, String version, Integer rank, Long productId, Double score, Long likes,
        Long views, Long orderCount) {
        this.periodKey = periodKey;
        this.version = version;
        this.rank = rank;
        this.productId = productId;
        this.score = score;
        this.likes = likes;
        this.views = views;
        this.orderCount = orderCount;
        this.generatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public String getVersion() {
        return version;
    }

    public Integer getRank() {
        return rank;
    }

    public Long getProductId() {
        return productId;
    }

    public Double getScore() {
        return score;
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
