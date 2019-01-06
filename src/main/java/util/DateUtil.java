package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String dateToString(LocalDate date) {
        return date != null ? date.format(dateTimeFormatter) : "";
    }

    public static LocalDate parseLocalDate(String date) {
        return date != null && !date.isEmpty() ? LocalDate.parse(date, dateTimeFormatter) : null;
    }
}
