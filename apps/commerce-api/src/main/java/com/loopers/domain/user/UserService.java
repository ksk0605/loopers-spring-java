package com.loopers.domain.user;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String userId, Gender gender, String birthDate, String email) {
        userRepository.find(userId).ifPresent(user -> {
            throw new CoreException(ErrorType.CONFLICT, "이미 가입한 ID입니다. [userId = " + userId + "]");
        });
        return userRepository.save(
            new User(userId, gender, birthDate, email)
        );
    }

    @Transactional(readOnly = true)
    public User getUser(String userId) {
        return userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 유저 ID 입니다. [userId = " + userId + "]"));
    }
}
