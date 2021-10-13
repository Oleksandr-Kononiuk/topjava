package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoImpl implements MealDao {

    private List<Meal> meals = MealsUtil.generateMealList();
    private static final AtomicLong COUNTER = new AtomicLong(7); // todo first 7 value is hardcoded in MealUtils

    @Override
    public void add(Meal meal) {
        meal.setId(COUNTER.incrementAndGet());
        meals.add(meal);
    }

    @Override
    public Meal findById(long id) {
        return meals.stream()
                .filter((meal) -> meal.getId() == id)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Meal> findAll() {
        return meals;
    }

    @Override
    public void update(long id, Meal meal) {
        int index = 0;
        for(int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == id) {
                meals.remove(i);
                index = i;
                break;
            }
        }
        meals.add(index, meal);
        //? meals.replaceAll(m -> meal);
    }

    @Override
    public void delete(long id) {
        meals.remove(findById(id));
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
