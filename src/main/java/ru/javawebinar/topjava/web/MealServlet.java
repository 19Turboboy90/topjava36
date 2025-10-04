package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorageInMemory;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.JspHelper;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final MealStorageInMemory mealStorage = new MealStorageInMemory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            showMealsInfo(req, resp);
        } else {
            switch (action) {
                case "addMeal": {
                    req.getRequestDispatcher(JspHelper.prefixPath("meal-create")).forward(req, resp);
                }
                break;
                case "delete": {
                    getMealId(req).ifPresent(mealStorage::delete);
                    resp.sendRedirect("meals");
                }
                break;
                case "update": {
                    getMealId(req).ifPresent(id -> {
                        Optional<Meal> getMeal = mealStorage.findById(id);
                        getMeal.ifPresent(meal -> req.setAttribute("meal", meal));
                    });
                    req.getRequestDispatcher(JspHelper.prefixPath("meal-create")).forward(req, resp);
                }
                break;
                default: {
                    showMealsInfo(req, resp);
                }
            }
        }
    }

    private void showMealsInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("meals",
                MealsUtil.filteredByStreams(
                        mealStorage.findAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        MealsUtil.CALORIES_PER_DAY));

        req.getRequestDispatcher(JspHelper.prefixPath("meals")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Meal meal = buildMeal(req);

        if (meal.getId() != null) {
            mealStorage.update(meal);
        } else {
            mealStorage.create(meal);
        }
        resp.sendRedirect("meals");
    }

    private static Meal buildMeal(HttpServletRequest req) {
        Optional<Integer> mealId = getMealId(req);
        return new Meal(
                mealId.orElse(null),
                DateTimeUtil.formatDateTime(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories")));

    }

    private static Optional<Integer> getMealId(HttpServletRequest req) {
        String parameter = req.getParameter("mealId");
        log.info("method getMealId, param = {}", parameter);
        if (parameter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(parameter));
    }
}