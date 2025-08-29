package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> calculateCalories = getCalculateCaloriesByCycle(meals);

        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(meal -> {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesPerDay < calculateCalories.get(meal.getDateTime().toLocalDate())));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> calculateCalories = getCalculateCaloriesByStream(meals);

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesPerDay < calculateCalories.get(meal.getDateTime().toLocalDate())))
                .collect(Collectors.toList());
    }

    private static Map<LocalDate, Integer> getCalculateCaloriesByCycle(List<UserMeal> meals) {
        Map<LocalDate, Integer> calculateCalories = new HashMap<>();
        meals.forEach(meal ->
                calculateCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        return calculateCalories;
    }

    private static Map<LocalDate, Integer> getCalculateCaloriesByStream(List<UserMeal> meals) {
        return meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)
                ));
    }
}
