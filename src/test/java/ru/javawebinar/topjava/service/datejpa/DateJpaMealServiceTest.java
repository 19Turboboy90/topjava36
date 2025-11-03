package ru.javawebinar.topjava.service.datejpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DateJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal meal = service.getByIdWithUser(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);

        MealTestData.MEAL_MATCHER.assertMatch(meal, MealTestData.adminMeal1);
        UserTestData.USER_MATCHER.assertMatch(meal.getUser(), UserTestData.admin);
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(MealTestData.ADMIN_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByIdWithUser(NOT_FOUND, USER_ID));
    }
}