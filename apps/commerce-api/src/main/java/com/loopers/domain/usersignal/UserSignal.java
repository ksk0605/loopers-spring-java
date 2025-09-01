package com.loopers.domain.usersignal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_signal", uniqueConstraints = @UniqueConstraint(columnNames = {"target_id", "target_type"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSignal {
    private static final long MINIMUM_LIKE_COUNT = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private Long likeCount;

    private Long views;

    public UserSignal(Long targetId, TargetType type) {
        this.targetId = targetId;
        this.targetType = type;
        this.likeCount = 0L;
        this.views = 0L;
    }

    public void incrementViews() {
        views++;
    }

    public void updateLikeCount(Long count) {
        if (count < MINIMUM_LIKE_COUNT) {
            throw new IllegalStateException("좋아요 수는 0보다 작을 수 없습니다.");
        }
        this.likeCount = count;
    }
}
