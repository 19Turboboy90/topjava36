package ru.javawebinar.topjava.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<K, T> {
    T save(T entity);

    Optional<T> findById(K id);

    List<T> findAll();

    void update(T entity);

    boolean delete(K id);
}