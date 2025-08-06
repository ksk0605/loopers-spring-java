package com.loopers.infrastructure.point;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.loopers.domain.point.PointHistory;
import com.loopers.domain.point.PointHistoryRepository;

@Repository
public class PointHistoryCoreRepository implements PointHistoryRepository {

    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    public PointHistoryCoreRepository(PointHistoryJpaRepository pointHistoryJpaRepository) {
        this.pointHistoryJpaRepository = pointHistoryJpaRepository;
    }

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryJpaRepository.save(pointHistory);
    }

    @Override
    public List<PointHistory> findAll(String userId) {
        return pointHistoryJpaRepository.findAllByUserId(userId);
    }
}
