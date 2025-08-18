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
    private final LikeSummaryRepository likeSummaryRepository;

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
    public List<Like> getAll(Long userId, LikeTargetType targetType) {
        return likeRepository.findAll(userId, targetType);
    }

    @Transactional(readOnly = true)
    public List<TargetLikeCount> getAllByTargetIn(List<Long> productIds, LikeTargetType likeTargetType) {
        List<LikeTarget> targets = productIds.stream().map(id -> new LikeTarget(id, likeTargetType)).toList();
        return likeSummaryRepository.findAllByTargetIn(targets);
    }
}
