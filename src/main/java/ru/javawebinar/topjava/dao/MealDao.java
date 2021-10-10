package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

public interface MealDao {

    void create(Meal meal);
    Meal read(long id);
    Meal update(long id, Meal meal);
    void delete(long id);
}
