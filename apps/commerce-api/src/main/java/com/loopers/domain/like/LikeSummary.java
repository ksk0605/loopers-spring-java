package com.loopers.domain.like;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "like_summary",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"target_id", "target_type"}
    )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeSummary {
    private static final int MINIMUM_LIKE_THRESHOLD = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long likeCount;

    @Embedded
    private LikeTarget target;

    public LikeSummary(Long targetId, LikeTargetType targetType) {
        this.target = new LikeTarget(targetId, targetType);
        this.likeCount = 0L;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount <= MINIMUM_LIKE_THRESHOLD) {
            throw new IllegalArgumentException("좋아요 카운트는 0 이상이어야 합니다.");
        }
        this.likeCount--;
    }
}
