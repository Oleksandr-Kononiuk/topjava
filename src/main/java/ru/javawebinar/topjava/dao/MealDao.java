package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.UserMeal;

public interface MealDao {

    void create(UserMeal meal);
    UserMeal read(long id);
    UserMeal update(long id, UserMeal meal);
    void delete(long id);
}
