package com.loopers.infrastructure.like;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.LikeTargetType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeCoreRepository implements LikeRepository {
    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(like);
    }

    @Override
    public boolean exists(Long userId, LikeTarget target) {
        return likeJpaRepository.existsByUserIdAndTarget(userId, target);
    }

    @Override
    public void delete(Long userId, LikeTarget target) {
        likeJpaRepository.deleteByUserIdAndTarget(userId, target);
    }

    @Override
    public Long count(LikeTarget likeTarget) {
        return likeJpaRepository.countByTarget(likeTarget);
    }

    @Override
    public List<Like> findAll(Long userId, LikeTargetType targetType) {
        return likeJpaRepository.findAllByUserIdAndTargetType(userId, targetType);
    }
}
