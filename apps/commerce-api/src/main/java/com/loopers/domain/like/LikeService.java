package com.loopers.domain.like;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeSummaryModifier likeSummaryModifier;

    @Transactional
    public void like(Long userId, Long targetId, LikeTargetType targetType) {
        if (likeRepository.exists(userId, new LikeTarget(targetId, targetType))) {
            return;
        }
        Like like = new Like(userId, targetId, targetType);
        likeRepository.save(like);
        likeSummaryModifier.increment(like.getTarget());
    }

    @Transactional
    public void unlike(Long userId, Long targetId, LikeTargetType targetType) {
        LikeTarget target = new LikeTarget(targetId, targetType);
        if (!likeRepository.exists(userId, target)) {
            return;
        }
        likeRepository.delete(userId, target);
        likeSummaryModifier.decrement(target);
    }

    @Transactional(readOnly = true)
    public Long count(Long targetId, LikeTargetType targetType) {
        return likeRepository.count(new LikeTarget(targetId, targetType));
    }

    @Transactional(readOnly = true)
    public List<LikeInfo> getAll(Long userId, LikeTargetType targetType) {
        return likeRepository.findAll(userId, targetType)
            .stream()
            .map(LikeInfo::from)
            .toList();
    }
}
