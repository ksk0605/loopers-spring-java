package com.loopers.domain.like;

import static com.loopers.support.util.RequireUtils.requireNotNull;

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
@Table(name = "user_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Embedded
    private LikeTarget target;

    public Like(Long userId, Long targetId, LikeTargetType type) {
        this.userId = requireNotNull(userId, "유저 ID는 필수입니다.");
        this.target = new LikeTarget(targetId, type);
    }
}
