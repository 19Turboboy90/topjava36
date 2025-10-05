package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealStorage implements Storage<Integer, Meal> {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealStorage.class);

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::create);
    }

    @Override
    public Meal create(Meal entity) {
        log.info("method save, meal = {}", entity);
        int newId = id.incrementAndGet();
        log.info("method save, new meal id = {}", newId);
        Meal newMeal = new Meal(newId, entity.getDateTime(), entity.getDescription(), entity.getCalories());
        meals.put(newId, newMeal);
        return newMeal;
    }

    @Override
    public Optional<Meal> findById(Integer id) {
        log.info("method findById, id = {}", id);
        return Optional.of(meals.get(id));
    }

    @Override
    public List<Meal> findAll() {
        log.info("method findAll");
        return new ArrayList<>(meals.values());
    }

    @Override
    public void update(Meal entity) {
        log.info("method update, meal = {}", entity);
        meals.computeIfPresent(entity.getId(), (id, meal) ->
                new Meal(id, entity.getDateTime(), entity.getDescription(), entity.getCalories()));
    }

    @Override
    public boolean delete(Integer id) {
        log.info("method delete, id = {}", id);
        return meals.remove(id) != null;
    }
}