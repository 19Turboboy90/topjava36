package ru.javawebinar.topjava.service;

import annotation.IT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@IT
@RunWith(SpringRunner.class)
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID - 100, USER_ID));
    }

    @Test
    public void getMealByNotExistUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID - 100));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test()
    public void deleteNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID - 100, USER_ID));
    }

    @Test()
    public void deleteMealByNotExistUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, USER_ID - 100));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> mealsByFilter = service.getBetweenInclusive(
                LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 30),
                USER_ID);
        assertMatch(mealsByFilter, meal3, meal2, meal1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, meals());
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdatedMeal();
        service.update(updatedMeal, USER_ID);
        assertMatch(service.get(updatedMeal.getId(), USER_ID), updatedMeal);
    }

    @Test
    public void create() {
        Meal meal = new Meal(LocalDateTime.of(2025, 10, 20, 6, 0), "test", 1000);
        Meal createMeal = service.create(meal, USER_ID);
        Integer id = createMeal.getId();
        meal.setId(id);
        assertMatch(createMeal, meal);
        assertMatch(service.get(createMeal.getId(), USER_ID), meal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500), USER_ID));
    }
}