package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    void add(Meal meal);
    Meal findById(long id);
    List<Meal> findAll();
    void update(long id, Meal meal);
    void delete(long id);
}
