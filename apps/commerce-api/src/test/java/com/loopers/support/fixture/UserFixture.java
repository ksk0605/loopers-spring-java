package com.loopers.support.fixture;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;

public class UserFixture {
    private String userId = "testUser";
    private String email = "testUser@test.com";
    private Gender gender = Gender.MALE;
    private String birthDate = "2000-01-01";

    public static UserFixture anUser() {
        return new UserFixture();
    }

    public UserFixture userId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserFixture email(String email) {
        this.email = email;
        return this;
    }

    public UserFixture gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public UserFixture birthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public User build() {
        return new User(
            userId,
            gender,
            birthDate,
            email
        );
    }

    private UserFixture() {
    }
}
