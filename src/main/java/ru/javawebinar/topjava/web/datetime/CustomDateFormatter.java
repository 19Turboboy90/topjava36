package ru.javawebinar.topjava.web.datetime;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CustomDateFormatter implements Formatter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) {
        return LocalDate.parse(text, FORMATTER);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.toString();
    }
}