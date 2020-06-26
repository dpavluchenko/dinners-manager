package com.pavliuchenko.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public class Period {
    private LocalDate startDate;
    private LocalDate endDate;

    public int calcBetweenPeriod() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public void minusStartDate(int days) {
        startDate = startDate.minusDays(days);
    }
}
