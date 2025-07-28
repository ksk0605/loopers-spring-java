package com.loopers.infrastructure;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import com.loopers.domain.like.LikeTarget;
import com.loopers.infrastructure.user.LikeJpaRepository;

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
}
