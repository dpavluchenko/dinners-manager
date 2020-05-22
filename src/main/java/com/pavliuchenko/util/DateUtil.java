package com.pavliuchenko.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String dateToString(LocalDate date) {
        return date != null ? date.format(dateTimeFormatter) : "";
    }

    public static LocalDate parseLocalDate(String date) {
        return date != null && !date.isEmpty() ? LocalDate.parse(date, dateTimeFormatter) : null;
    }

    public static String getDayOfWeek(LocalDate date) {
        if (date.isEqual(LocalDate.now())) {
            return "Today";
        } else {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }
    }
}
