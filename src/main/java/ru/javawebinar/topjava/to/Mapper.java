package ru.javawebinar.topjava.to;

public interface Mapper<F, T> {
    T mapToMeal(F object);

    F mapToDto(T object);
}