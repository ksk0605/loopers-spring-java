package com.loopers.domain.rank;

import lombok.Getter;

@Getter
public class Period {
    private final PeriodKey periodKey;
    private final PeriodType periodType;

    public Period(PeriodKey periodKey, PeriodType periodType) {
        if (periodKey == null) {
            throw new IllegalArgumentException("PeriodKey는 null일 수 없습니다.");
        }
        if (periodType == null) {
            throw new IllegalArgumentException("PeriodType은 null일 수 없습니다.");
        }

        periodKey.validateFormat(periodType);

        this.periodKey = periodKey;
        this.periodType = periodType;
    }

    public static Period of(String periodKey, String periodType) {
        return new Period(new PeriodKey(periodKey), PeriodType.from(periodType));
    }
}
