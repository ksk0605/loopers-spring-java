package com.loopers.interfaces.api.user;

import java.time.LocalDate;

import com.loopers.application.user.UserInfo;
import com.loopers.domain.user.Gender;

public class UserV1Dto {
    public record CreateUserRequest(
        String userId,
        Gender gender,
        String birthDate,
        String email
    ) {
    }

    public record UserResponse(
        Long id,
        String userId,
        Gender gender,
        LocalDate birthDate,
        String email
    ) {
        public static UserResponse from(UserInfo userInfo) {
            return new UserResponse(
                userInfo.id(),
                userInfo.userId(),
                userInfo.gender(),
                userInfo.birthDate(),
                userInfo.email()
            );
        }
    }
}
