package ru.javawebinar.topjava.service.datejpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.List;

@ActiveProfiles(Profiles.DATAJPA)
public class DateJpaUserTest extends AbstractUserServiceTest {

    @Test
    public void getUserByIdWithMeals() {
        User user = service.getByIdWithMeals(UserTestData.ADMIN_ID);
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.admin);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.mealsOfAdmin);
    }

    @Test
    public void getUserByIdWithoutMeals() {
        User user = service.getByIdWithMeals(UserTestData.GUEST_ID);
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.guest);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), List.of());
    }
}