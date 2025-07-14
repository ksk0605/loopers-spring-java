package com.loopers.domain.point;

import org.springframework.stereotype.Component;

import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@Component
public class PointService {
    private final UserRepository userRepository;

    public PointService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public int getMyPoint(String userId) {
        User user = userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 유저 ID 입니다. [userId = " + userId + "]"));
        return user.getPoint();
    }
}
