package com.loopers.domain.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;

@Getter
public class Like {
    private Long userId;
    private LikeTarget target;

    public Like(Long userId, Long targetId, LikeTargetType type) {
        if (userId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "유저 ID는 필수입니다.");
        }
        if (targetId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 타겟 ID는 필수입니다.");
        }
        if (type == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "좋아요 타겟 타입은 필수입니다.");
        }
        this.userId = userId;
        this.target = new LikeTarget(targetId, type);
    }
}
