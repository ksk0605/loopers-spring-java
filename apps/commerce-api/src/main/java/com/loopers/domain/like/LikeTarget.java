package com.loopers.domain.like;

import static com.loopers.support.util.RequireUtils.requireNotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode(of = {"id", "type"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeTarget {
    @Column(name = "target_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private LikeTargetType type;

    public LikeTarget(Long id, LikeTargetType type) {
        this.id = requireNotNull(id, "좋아요 타겟 ID는 필수입니다.");
        this.type = requireNotNull(type, "좋아요 타겟 타입은 필수입니다.");
    }

    public Long id() {
        return id;
    }

    public LikeTargetType type() {
        return type;
    }
}
