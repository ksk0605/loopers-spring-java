package com.loopers.domain.like;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeSummary {
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
        this.likeCount--;
    }
}
