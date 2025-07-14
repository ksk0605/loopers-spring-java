package com.loopers.application.user;

import org.springframework.stereotype.Component;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;

@Component
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public UserInfo createUser(
        String userId,
        Gender gender,
        String birthDate,
        String email
    ) {
        User user = userService.createUser(userId, gender, birthDate, email);
        return UserInfo.from(user);
    }

    public UserInfo getUser(String userId) {
        User user = userService.getUser(userId);
        return UserInfo.from(user);
    }
}
