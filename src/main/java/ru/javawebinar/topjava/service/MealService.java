package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int userId) {
        log.info("save, meal = {}, userId = {}", meal, userId);
        return repository.save(meal, userId);
    }

    public void delete(int mealId, int userId) {
        log.info("delete, mealId = {}, userId = {}", mealId, userId);
        checkNotFound(repository.delete(mealId, userId), mealId);
    }

    public Meal get(int mealId, int userId) {
        log.info("get, mealId = {}, userId = {}", mealId, userId);
        return checkNotFound(repository.get(mealId, userId), mealId);
    }

    public List<MealTo> getAll(int userId) {
        log.info("getAll, userId = {}", userId);
        return getTos(repository.getAll(userId), DEFAULT_CALORIES_PER_DAY);
    }


    public List<MealTo> filterByDateAndTime(LocalDate fromDate, LocalDate toDate,
                                            LocalTime fromTime, LocalTime toTime, int userId) {
        log.info("filterByDateAndTime, fromDate = {}, toDate = {}, fromTime = {}, toTime = {}, userId = {}",
                fromDate, toDate, fromTime, toTime, userId);
        return getFilteredTos(repository.filterByDate(fromDate, toDate, userId),
                DEFAULT_CALORIES_PER_DAY, fromTime, toTime);

    }

    public void update(Meal meal, int userId) {
        log.info("update, meal = {}, userId = {}", meal, userId);
        checkNotFound(repository.save(meal, userId), meal.getId());
    }
}