package com.loopers.application.user;

import org.springframework.stereotype.Component;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@Component
public class UserFacade {

    private final UserService userService;

    public UserFacade(UserService userService) {
        this.userService = userService;
    }

    public UserInfo createUser(
        String userId,
        String gender,
        String birthDate,
        String email
    ) {
        User user = userService.createUser(userId, Gender.getGender(gender), birthDate, email);
        return UserInfo.from(user);
    }

    public UserInfo getUser(String userId) {
        User user = userService.getUser(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        return UserInfo.from(user);
    }
}
