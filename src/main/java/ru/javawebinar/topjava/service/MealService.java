package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealMapper;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);
    private final MealRepository repository;
    private final MealMapper mealMapper;

    public MealService(MealRepository repository, MealMapper mealMapper) {
        this.repository = repository;
        this.mealMapper = mealMapper;
    }

    public MealTo save(MealTo mealTo, int userId) {
        log.info("save, meal = {}, userId = {}", mealTo, userId);
        checkNotFound(userId, userId);
        Meal meal = mealMapper.mapToMeal(mealTo);
        if (mealTo.isNew()) {
            meal.setUserId(userId);
        } else {
            checkNotFound(meal.getUserId() == userId, userId);
        }
        Meal saveMeal = repository.save(meal);
        return mealMapper.mapToDto(saveMeal);
    }

    public void delete(int mealId, int userId) {
        log.info("delete, mealId = {}, userId = {}", mealId, userId);
        checkNotFound(mealId, mealId);
        checkNotFound(userId, userId);
        Meal meal = repository.get(mealId);
        checkNotFound(meal.getUserId() == userId, userId);
        repository.delete(mealId);
    }

    public MealTo get(int mealId, int userId) {
        log.info("get, mealId = {}, userId  = {}", mealId, userId);
        checkNotFound(mealId, mealId);
        Meal meal = repository.get(mealId);
        checkNotFound(meal, mealId);
        checkNotFound(meal.getUserId() == userId, userId);
        return mealMapper.mapToDto(meal);
    }

    public List<MealTo> getAll(int userId) {
        log.info("getAll, userId = {}", userId);
        checkNotFound(userId, userId);
        return getTos(repository.getAll(userId), DEFAULT_CALORIES_PER_DAY);
    }


    public List<MealTo> filterByDate(LocalDate fromDate, LocalDate toDate,
                                     LocalTime fromTime, LocalTime toTime, int userId) {
        log.info("filterByDateAndTime, fromDate = {}, toDate = {}, userId = {}, fromTime = {}, toTime = {}",
                fromDate, toDate, fromTime, toTime, userId);
        checkNotFound(userId, userId);
        return getTos(repository.filterByDate(fromDate, toDate, fromTime, toTime, userId), DEFAULT_CALORIES_PER_DAY);
    }
}