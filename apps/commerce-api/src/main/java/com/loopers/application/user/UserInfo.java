package com.loopers.application.user;

import java.time.LocalDate;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;

public record UserInfo(
    Long id,
    String userId,
    Gender gender,
    LocalDate birthDate,
    String email
) {
    public static UserInfo from(User user) {
        return new UserInfo(
            user.getId(),
            user.getUserId(),
            user.getGender(),
            user.getBirthDate(),
            user.getEmail()
        );
    }
}
