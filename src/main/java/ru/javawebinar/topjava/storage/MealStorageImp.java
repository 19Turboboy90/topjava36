package ru.javawebinar.topjava.storage;

import lombok.extern.slf4j.Slf4j;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MealStorageImp implements Storage<Integer, Meal> {

    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger id = new AtomicInteger(0);

    {
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак", 500));
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед", 1000));
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                "Ужин", 500));
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                "Еда на граничное значение", 100));
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                "Завтрак", 1000));
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                "Обед", 500));
        meals.put(id.incrementAndGet(), new Meal(id.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                "Ужин", 410));
    }

    @Override
    public Meal save(Meal entity) {
        log.info("Method save, meal = {}", entity);
        int newId = id.incrementAndGet();
        entity.setId(newId);
        meals.put(newId, entity);
        log.info("new meal id = {}", entity.getId());
        return entity;
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        log.info("Method findById, id = {}", id);
        return Optional.of(meals.get(id));
    }

    @Override
    public List<Meal> findAll() {
        log.info("Method findAll");
        return new ArrayList<>(meals.values());
    }

    @Override
    public void update(Meal entity) {
        log.info("Method update, meal = {}", entity);
        Meal meal = meals.get(entity.getId());
        meal.setId(entity.getId());
        meal.setDateTime(entity.getDateTime());
        meal.setDescription(entity.getDescription());
        meal.setCalories(entity.getCalories());
    }

    @Override
    public boolean delete(Integer id) {
        log.info("Method delete, id = {}", id);
        return meals.remove(id) != null;
    }
}