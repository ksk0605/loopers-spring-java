package com.loopers.infrastructure.point;

import org.springframework.stereotype.Repository;

import com.loopers.domain.point.PointHistory;
import com.loopers.domain.point.PointRepository;

@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    public PointRepositoryImpl(PointJpaRepository pointJpaRepository) {
        this.pointJpaRepository = pointJpaRepository;
    }

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointJpaRepository.save(pointHistory);
    }
}
