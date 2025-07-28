package com.loopers.infrastructure.like;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import com.loopers.domain.like.LikeTarget;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository likeJpaRepository;

    @Override
    public Like save(Like like) {
        return likeJpaRepository.save(like);
    }

    @Override
    public boolean existsByTarget(LikeTarget target) {
        return likeJpaRepository.existsByTarget(target);
    }

    @Override
    public void deleteByTarget(LikeTarget target) {
        likeJpaRepository.deleteByTarget(target);
    }

    @Override
    public Long count(LikeTarget likeTarget) {
        return likeJpaRepository.countByTarget(likeTarget);
    }
}
