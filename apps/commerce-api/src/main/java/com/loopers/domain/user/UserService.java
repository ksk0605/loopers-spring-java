package com.loopers.domain.user;

import java.util.Optional;

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
    public Optional<User> findUser(String userId) {
        return userRepository.find(userId);
    }
}
