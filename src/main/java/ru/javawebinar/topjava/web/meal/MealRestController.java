package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public MealTo save(MealTo mealDto) {
        return service.save(mealDto, authUserId());
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public MealTo get(int id) {
        return service.get(id, authUserId());
    }

    public List<MealTo> getAll() {
        return service.getAll(authUserId());
    }

    public List<MealTo> filterByDateAndTime(LocalDate fromDate, LocalDate toDate,
                                            LocalTime fromTime, LocalTime toTime) {
        return service.filterByDate(fromDate, toDate, fromTime, toTime, authUserId());
    }
}