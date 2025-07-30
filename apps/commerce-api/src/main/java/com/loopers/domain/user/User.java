package com.loopers.domain.user;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    private static final String USER_ID_PATTERN = "^[a-zA-Z0-9]+$";
    private static final int MAX_USER_ID_LENGTH = 10;
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String BIRTH_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final int INITIAL_POINT = 0;

    private String userId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private String email;

    private int point;

    protected User() {
    }

    public User(String userId, Gender gender, String birthDate, String email) {
        validateUserId(userId);
        validateGender(gender);
        validateBirthDate(birthDate);
        validateEmail(email);

        this.userId = userId;
        this.gender = gender;
        this.birthDate = LocalDate.parse(birthDate);
        this.email = email;
        this.point = INITIAL_POINT;
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID는 비어있을 수 없습니다.");
        }
        if (!userId.matches(USER_ID_PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID는 영문자와 숫자만 허용됩니다.");
        }
        if (userId.length() > MAX_USER_ID_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST, "ID는 10자 이내여야 합니다.");
        }
    }

    private void validateGender(Gender gender) {
        if (gender == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "성별은 필수 정보 입니다.");
        }
    }

    private void validateBirthDate(String birthDate) {
        if (birthDate == null || birthDate.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 비어있을 수 없습니다.");
        }
        if (!birthDate.matches(BIRTH_DATE_PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 yyyy-MM-dd 형식이어야 합니다.");
        }
        try {
            LocalDate.parse(birthDate);
        } catch (DateTimeParseException e) {
            throw new CoreException(ErrorType.BAD_REQUEST, "유효하지 않은 날짜입니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 비어있을 수 없습니다.");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
        }
    }

    public void updatePoint(int balance) {
        this.point = balance;
    }

    public String getUserId() {
        return userId;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public int getPoint() {
        return point;
    }

    public void chargePoint(int amount) {
        this.point += amount;
    }
}
