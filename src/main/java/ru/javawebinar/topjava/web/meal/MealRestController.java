package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("meal= {}", meal);
        return service.save(meal, authUserId());
    }

    public void delete(int mealId) {
        log.info("delete, mealId = {}", mealId);
        service.delete(mealId, authUserId());
    }

    public Meal get(int mealId) {
        log.info("get, mealId = {}", mealId);
        return service.get(mealId, authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
    }

    public void update(Meal meal, int userId) {
        log.info("update, meal= {}, userId = {}", meal, userId);
        service.update(meal, userId);
    }

    public List<MealTo> filterByDateAndTime(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        log.info("filterByDateAndTime, fromDate = {}, toDate = {}, fromTime = {}, toTime = {}",
                fromDate, toDate, fromTime, toTime);
        return service.filterByDateAndTime(
                fromDate == null ? LocalDate.MIN : fromDate,
                toDate == null ? LocalDate.MAX : toDate.plusDays(1),
                fromTime == null ? LocalTime.MIN : fromTime,
                toTime == null ? LocalTime.MAX : toTime,
                authUserId());
    }
}