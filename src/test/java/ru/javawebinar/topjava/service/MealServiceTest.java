package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;

public class MealServiceTest extends AbstractBaseEntityServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_MEAL, USER_ID));
    }

    @Test
    public void getMealByNotExistUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, NOT_FOUND));
    }

    @Test
    public void getMealAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, GUEST_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test()
    public void deleteNotExistMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_MEAL, USER_ID));
    }

    @Test()
    public void deleteMealByNotExistUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, NOT_FOUND));
    }

    @Test()
    public void deleteMealAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, GUEST_ID));
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
        Meal updatedMeal = MealTestData.getUpdated();
        service.update(updatedMeal, USER_ID);
        Meal expected = MealTestData.getUpdated();
        assertMatch(service.get(updatedMeal.getId(), USER_ID), expected);
    }

    @Test
    public void updateMealAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.getUpdated(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal createMeal = service.create(MealTestData.getNew(), USER_ID);
        Integer newId = createMeal.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);

        assertMatch(createMeal, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(meal1.getDateTime(), "Завтрак", 500), USER_ID));
    }
}