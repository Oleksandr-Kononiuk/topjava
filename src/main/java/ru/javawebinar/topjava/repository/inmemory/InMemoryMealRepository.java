package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
//todo refactor by using ValidationUtil

//    {
//        MealsUtil.meals.forEach(() -> this.save());
//    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (repository.get(userId) == null) {
            repository.put(userId, new ConcurrentHashMap<>());
            save(userId, meal);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId)
                .computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        if (repository.get(userId) == null)
            throw new NotFoundException("User not found with id:" + userId); // todo return false???
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        if (repository.get(userId) == null)
            throw new NotFoundException("User not found with id:" + userId);
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        if (repository.get(userId) == null)
            throw new NotFoundException("User not found with id:" + userId);
        return repository.get(userId).values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}

