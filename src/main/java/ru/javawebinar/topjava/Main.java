package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final int CALORIES_PER_DAY = 2000;
    public static final LocalTime START_TIME = LocalTime.of(7, 0);
    public static final LocalTime END_TIME = LocalTime.of(12, 0);

    public static final List<UserMeal> MEALS = Arrays.asList(
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                    "Завтрак", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                    "Обед", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                    "Ужин", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                    "Еда на граничное значение", 100),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                    "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                    "Обед", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                    "Ужин", 410)
    );

    public static void main(String[] args) {
        List<UserMealWithExcess> mealsTo = UserMealsUtil.filteredByCycles(MEALS, START_TIME, END_TIME, CALORIES_PER_DAY);
        mealsTo.forEach(System.out::println);

        System.out.println(UserMealsUtil.filteredByStreams(MEALS, START_TIME, END_TIME, CALORIES_PER_DAY));
    }
}
