package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

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
        checkNotFound(userId, userId);
        return repository.save(meal, userId);
    }

    public void delete(int mealId, int userId) {
        log.info("delete, mealId = {}, userId = {}", mealId, userId);
        checkNotFound(mealId, mealId);
        checkNotFound(userId, userId);
        repository.delete(mealId, userId);
    }

    public Meal get(int mealId, int userId) {
        log.info("get, mealId = {}, userId = {}", mealId, userId);
        checkNotFound(mealId, mealId);
        checkNotFound(userId, userId);
        return repository.get(mealId, userId);
    }

    public List<MealTo> getAll(int userId) {
        log.info("getAll, userId = {}", userId);
        checkNotFound(userId, userId);
        return MealsUtil.getTos(repository.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }


//    public List<MealTo> filterByDateAndTime(LocalDate fromDate, LocalDate toDate,
//                                            LocalTime fromTime, LocalTime toTime, int userId) {
//        log.info("filterByDateAndTime, fromDate = {}, toDate = {}, fromTime = {}, toTime = {}, userId = {}",
//                fromDate, toDate, fromTime, toTime, userId);
//        repository.getAll(userId)
//
//    }

    public void update(Meal meal, int userId) {
        log.info("update, meal = {}, userId = {}", meal, userId);
        checkNotFound(meal, meal.getId());
        checkNotFound(userId, userId);
        repository.save(meal, userId);
    }
}