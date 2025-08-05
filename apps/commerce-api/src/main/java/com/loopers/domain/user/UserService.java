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
    public UserInfo createUser(UserCommand.Create command) {
        userRepository.find(command.userId()).ifPresent(user -> {
            throw new CoreException(ErrorType.CONFLICT, "이미 가입한 ID입니다. [userId = " + command.userId() + "]");
        });
        User user = userRepository.save(
            new User(
                command.userId(),
                Gender.valueOf(command.gender()),
                command.birthDate(),
                command.email()
            )
        );
        return UserInfo.from(user);
    }

    @Transactional(readOnly = true)
    public UserInfo get(String userId) {
        User user = userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        return UserInfo.from(user);
    }

    public UserInfo updatePoint(String userId, int balance) {
        User user = userRepository.find(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        user.updatePoint(balance);
        return UserInfo.from(userRepository.save(user));
    }

    @Transactional
    public void pay(Long aLong, BigDecimal totalPrice) {
        User user = userRepository.find(aLong).orElseThrow();
        user.usePoint(totalPrice.intValue());
        userRepository.save(user);
    }
}
