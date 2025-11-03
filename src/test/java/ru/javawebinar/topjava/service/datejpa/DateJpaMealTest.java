package ru.javawebinar.topjava.service.datejpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DateJpaMealTest extends AbstractMealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal meal = service.getByIdWithUser(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);

        MealTestData.MEAL_MATCHER.assertMatch(meal, MealTestData.adminMeal1);
        UserTestData.USER_MATCHER.assertMatch(meal.getUser(), UserTestData.admin);
    }
}