package com.loopers.domain.point;

import org.springframework.stereotype.Component;

@Component
public class PointService {
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public PointHistory earnHistory(String userId, int currentPoint, int amount) {
        return pointRepository.save(
            new PointHistory(userId, amount, currentPoint + amount, PointHistoryType.EARN)
        );
    }
}
