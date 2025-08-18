package com.loopers.application.user;

import java.time.LocalDate;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;

public record UserResult(
    Long id,
    String userId,
    Gender gender,
    String email,
    LocalDate birthDate
) {
    public static UserResult from(User user) {
        return new UserResult(
            user.getId(),
            user.getUserId(),
            user.getGender(),
            user.getEmail(),
            user.getBirthDate()
        );
    }
}
