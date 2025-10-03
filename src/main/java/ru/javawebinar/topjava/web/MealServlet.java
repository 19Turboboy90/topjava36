package ru.javawebinar.topjava.web;

import lombok.extern.slf4j.Slf4j;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorageImp;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.JspHelper;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

@WebServlet("/meals")
@Slf4j
public class MealServlet extends HttpServlet {
    private final MealStorageImp meals = new MealStorageImp();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            mealsInfo(req, resp);
        } else {
            switch (action) {
                case "addMeal": {
                    req.getRequestDispatcher(JspHelper.prefixPath("meal-create")).forward(req, resp);
                }
                break;
                case "delete": {
                    getMealId(req, "mealId").ifPresent(meals::delete);
                    resp.sendRedirect("meals");
                }
                break;
                case "update": {
                    getMealId(req, "mealId").ifPresent(id -> {
                        Optional<Meal> getMeal = meals.findById(id);
                        getMeal.ifPresent(meal -> req.setAttribute("meal", meal));
                    });
                    req.getRequestDispatcher(JspHelper.prefixPath("meal-create")).forward(req, resp);
                }
                break;
                default: {
                    mealsInfo(req, resp);
                }
            }
        }
    }

    private void mealsInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("meals",
                MealsUtil.filteredByStreams(
                        meals.findAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        MealsUtil.CALORIES_PER_DAY));

        req.getRequestDispatcher(JspHelper.prefixPath("meals")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Meal meal = buildMeal(req);

        if (getMealId(req, "mealId").isPresent()) {
            Integer id = getMealId(req, "mealId").get();
            meal.setId(id);
            meals.update(meal);
        } else {
            meals.save(meal);
        }
        resp.sendRedirect("meals");
    }

    private static Meal buildMeal(HttpServletRequest req) {
        return Meal.builder()
                .dateTime(DateTimeUtil.formatDateTime(req.getParameter("dateTime")))
                .description(req.getParameter("description"))
                .calories(Integer.parseInt(req.getParameter("calories")))
                .build();
    }

    private static Optional<Integer> getMealId(HttpServletRequest req, String mealId) {
        log.info("Method getMealId, param = {}", mealId);
        String parameter = req.getParameter(mealId);
        if (parameter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(parameter));
    }
}