package ru.javawebinar.topjava.to;

import ru.javawebinar.topjava.model.Meal;

public class MealMapper implements Mapper<MealTo, Meal> {
    @Override
    public Meal mapToMeal(MealTo object) {
        return new Meal(
                object.getId() != null ? object.getId() : null,
                object.getDateTime(),
                object.getDescription(),
                object.getCalories(),
                object.getUserId() != null ? object.getUserId() : null
        );
    }

    @Override
    public MealTo mapToDto(Meal object) {
        return new MealTo(
                object.getId(),
                object.getDateTime(),
                object.getDescription(),
                object.getCalories(),
                object.getUserId());
    }
}