package com.loopers.domain.like;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void like(Long userId, Long targetId, LikeTargetType targetType) {
        if (likeRepository.existsByTarget(new LikeTarget(targetId, targetType))) {
            return;
        }
        Like like = new Like(userId, targetId, targetType);
        likeRepository.save(like);
    }

    @Transactional
    public void unlike(Long userId, Long targetId, LikeTargetType targetType) {
        if (!likeRepository.existsByTarget(new LikeTarget(targetId, targetType))) {
            return;
        }
        likeRepository.deleteByTarget(new LikeTarget(targetId, targetType));
    }
}
