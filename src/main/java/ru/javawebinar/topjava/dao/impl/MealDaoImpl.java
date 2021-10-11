package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.NoSuchElementException;

public class MealDaoImpl implements MealDao {

    private List<Meal> meals = MealsUtil.generateMealList();

    @Override
    public void add(Meal meal) {
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
        meals.removeIf((meal -> meal.getId() == id));
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
