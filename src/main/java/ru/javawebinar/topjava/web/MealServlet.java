package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    private final MealRestController controller = new MealRestController(appCtx.getBean(MealService.class));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        MealTo mealTo = new MealTo(id.isEmpty() ? null : Integer.parseInt(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                authUserId());

        log.info(mealTo.isNew() ? "Create {}" : "Update {}", mealTo);
        controller.save(mealTo);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealTo mealTo = "create".equals(action) ?
                        new MealTo(
                                null,
                                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                                "",
                                1000,
                                authUserId()) :
                        controller.get(getId(request));
                request.setAttribute("meal", mealTo);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", getAllByDateOrTime(request));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private List<MealTo> getAllByDateOrTime(HttpServletRequest request) {
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String fromTime = request.getParameter("fromTime");
        String toTime = request.getParameter("toTime");

        return controller.filterByDateAndTime(
                fromDate == null || fromDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(fromDate),
                toDate == null || toDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(toDate),
                fromTime == null || fromTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(fromTime),
                toTime == null || toTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(toTime)
        );
    }

    @Override
    public void destroy() {
        appCtx.close();
    }
}
