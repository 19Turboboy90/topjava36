package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);


    private final Map<Integer, Map<Integer, Meal>> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals1.forEach(meal -> save(meal, 1));
        MealsUtil.meals2.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save meal = {}, userId = {}", meal, userId);

        Map<Integer, Meal> meals = mealsMap.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int mealId, int userId) {
        log.info("delete, mealId = {}, userId = {}", mealId, userId);
        Map<Integer, Meal> meals = mealsMap.get(userId);
        return meals != null && meals.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        log.info("get, mealId = {}, mealId = {}", mealId, userId);
        Map<Integer, Meal> getMeal = mealsMap.get(userId);
        return getMeal.get(mealId) != null ? getMeal.get(mealId) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll, userId = {}", userId);
        return filterByDate(null, null, userId);
    }

    @Override
    public List<Meal> filterByDate(LocalDate fromDate, LocalDate toDate, int userId) {
        Collection<Meal> result = mealsMap.get(userId) != null ?
                mealsMap.get(userId).values() : Collections.emptyList();

        return result.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDate(), fromDate, toDate))
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(toList());
    }
}

