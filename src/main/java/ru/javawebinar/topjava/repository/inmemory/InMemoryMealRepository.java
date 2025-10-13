package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);


    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);

    }

    @Override
    public Meal save(Meal meal) {
        log.info("save meal = {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealsMap.put(meal.getId(), meal);
            return meal;
        }
        return mealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete, mealId = {}", id);
        return mealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        log.info("get, mealId = {}", id);
        return mealsMap.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll, userId = {}", userId);
        return mealsMap.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(toList());
    }

    @Override
    public List<Meal> filterByDate(LocalDate fromDate, LocalDate toDate,
                                   LocalTime fromTime, LocalTime toTime, int userId) {
        Collection<Meal> all = getAll(userId);
        return all.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDate(), fromDate, toDate))
                .filter(meal -> isBetweenHalfOpen(meal.getTime(), fromTime, toTime))
                .collect(toList());
    }
}

