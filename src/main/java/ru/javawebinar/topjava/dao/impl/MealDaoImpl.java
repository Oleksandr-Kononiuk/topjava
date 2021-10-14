package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoImpl implements MealDao {

    private List<Meal> meals = new CopyOnWriteArrayList<>(MealsUtil.generateMealList());
    private static final AtomicLong COUNTER = new AtomicLong(7); // todo first 7 value is hardcoded in MealUtils

    @Override
    public void add(Meal meal) {
        meal.setId(COUNTER.incrementAndGet());
        meals.add(meal);
    }

    @Override
    public Meal findById(long id) {
        for (Meal meal : meals) {
            if (meal.getId() == id) {
                return meal;
            }
        }
        throw new NoSuchElementException("Meal not found!");
    }

    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public void update(long id, Meal meal) {
        for (Meal m : meals) {
            if (m.getId() == id) {
                meal.setId(id);
                this.delete(id);
                meals.add(meal);
                break;
            }
        }
    }

    @Override
    public void delete(long id) {
        meals.forEach(meal -> {
            if (meal.getId() == id)
                meals.remove(meal);
        });
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
