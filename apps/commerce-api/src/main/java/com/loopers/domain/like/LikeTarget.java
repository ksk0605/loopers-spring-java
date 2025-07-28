package com.loopers.domain.like;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeTarget {
    @Column(name = "target_id")
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    LikeTargetType type;

    public LikeTarget(Long id, LikeTargetType type) {
        this.id = id;
        this.type = type;
    }

    public Long id() {
        return id;
    }

    public LikeTargetType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        LikeTarget that = (LikeTarget)o;
        return Objects.equals(id, that.id) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
