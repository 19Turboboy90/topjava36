package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    protected JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        log.info("delete: id = {}", id);
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping("/new")
    public String newForm(@ModelAttribute("meal") Meal meal) {
        log.info("newForm: meal = {}", meal);
        return "mealForm";
    }

    @PostMapping("/save")
    public String create(@ModelAttribute("meal") Meal meal) {
        log.info("create: meal = {}", meal);
        super.create(meal);
        return "redirect:/meals";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") int id, Model model) {
        log.info("editForm: id = {}, model = {}", id, model);
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("meal") Meal meal, HttpServletRequest request) {
        log.info("update: meal = {}", meal);
        super.update(meal, getId(request));
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        log.info("getId: paramId = {}", paramId);
        return Integer.parseInt(paramId);
    }

    @GetMapping("/filter")
    public String filter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        log.info("getBetween dates({} - {}) time({} - {})", startDate, endDate, startTime, endTime);

        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}