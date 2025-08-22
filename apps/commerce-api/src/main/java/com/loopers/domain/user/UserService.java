package com.loopers.domain.user;

import java.math.BigDecimal;

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
    public User createUser(UserCommand.Create command) {
        userRepository.find(command.userId()).ifPresent(user -> {
            throw new CoreException(ErrorType.CONFLICT, "이미 가입한 ID입니다. [userId = " + command.userId() + "]");
        });
        return userRepository.save(
            new User(
                command.userId(),
                Gender.valueOf(command.gender()),
                command.birthDate(),
                command.email()
            )
        );
    }

    @Transactional(readOnly = true)
    public User get(String userId) {
        return userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
    }

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [id = " + id + "]"));
    }

    public User updatePoint(String userId, int balance) {
        User user = userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        user.updatePoint(balance);
        return userRepository.save(user);
    }

    public void usePoint(String userId, BigDecimal amount) {
        User user = userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        user.usePoint(amount.intValue());
        userRepository.save(user);
    }
}
