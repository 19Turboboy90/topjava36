package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;

public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    private static final String PATTERN = "yyyy-MM-dd HH:mm";

    public static LocalDateTime formatDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime);
    }
}