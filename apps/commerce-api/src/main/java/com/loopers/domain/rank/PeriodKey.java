package com.loopers.domain.rank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class PeriodKey {
    private final String value;

    private static final Pattern DAILY_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern WEEKLY_PATTERN = Pattern.compile("^\\d{4}-W\\d{2}$");
    private static final Pattern MONTHLY_PATTERN = Pattern.compile("^\\d{6}$");

    private static final DateTimeFormatter DAILY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter MONTHLY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public PeriodKey(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("PeriodKey는 null이거나 빈 문자열일 수 없습니다.");
        }
        this.value = value.trim();
    }

    public void validateFormat(PeriodType periodType) {
        switch (periodType) {
            case DAILY -> validateDailyFormat();
            case WEEKLY -> validateWeeklyFormat();
            case MONTHLY -> validateMonthlyFormat();
        }
    }

    private void validateDailyFormat() {
        if (!DAILY_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Daily 형식이 올바르지 않습니다. yyyyMMdd 형식을 사용하세요. (예: 20250917)");
        }

        try {
            LocalDate.parse(value, DAILY_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 날짜입니다: " + value, e);
        }
    }

    private void validateWeeklyFormat() {
        if (!WEEKLY_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Weekly 형식이 올바르지 않습니다. yyyy-Www 형식을 사용하세요. (예: 2025-W38)");
        }

        try {
            String[] parts = value.split("-W");
            int year = Integer.parseInt(parts[0]);
            int week = Integer.parseInt(parts[1]);

            if (year < 1 || year > 9999) {
                throw new IllegalArgumentException("연도는 1-9999 범위여야 합니다: " + year);
            }

            if (week < 1 || week > 53) {
                throw new IllegalArgumentException("주는 1-53 범위여야 합니다: " + week);
            }

            LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
            int maxWeeks = firstDayOfYear.lengthOfYear() / 7 + 1;
            if (week > maxWeeks) {
                throw new IllegalArgumentException("해당 연도의 주 수를 초과했습니다. 최대 주 수: " + maxWeeks);
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Weekly 형식이 올바르지 않습니다: " + value, e);
        }
    }

    private void validateMonthlyFormat() {
        if (!MONTHLY_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Monthly 형식이 올바르지 않습니다. yyyyMM 형식을 사용하세요. (예: 202509)");
        }

        try {
            LocalDate.parse(value, MONTHLY_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 월입니다: " + value, e);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PeriodKey periodKey = (PeriodKey) obj;
        return value.equals(periodKey.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
