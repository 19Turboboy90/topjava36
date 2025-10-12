package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        log.info("meal= {}", meal);
        return service.save(meal, authUserId());
    }

    public void delete(int mealId) {
        service.delete(mealId, authUserId());
    }

    public Meal get(int mealId) {
        return service.get(mealId, authUserId());
    }

    public List<MealTo> getAll() {
        return service.getAll(authUserId());
    }

    public void update(Meal meal, int userId) {
        service.update(meal, userId);
    }
}