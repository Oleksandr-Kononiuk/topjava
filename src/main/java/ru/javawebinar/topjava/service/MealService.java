package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private UserService userService;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(mealRepository.get(id, userId), id);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void delete(int id, int userId) {
        checkNotFoundWithId(mealRepository.delete(id, userId), id);
    }

    public List<Meal> getBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return mealRepository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }

    @Cacheable("meals")
    public List<Meal> getAll(int userId) {
        return mealRepository.getAll(userId);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(mealRepository.save(meal, userId), meal.id());
    }

    @CacheEvict(value = "meals", allEntries = true)
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return mealRepository.save(meal, userId);
    }

    @Transactional(readOnly = true)
    public Map<User, List<Meal>> getUserMeals(int id) {
        return userService.getUserMeals(id);
    }

    @Transactional(readOnly = true)
    public Map<User, Meal> getUserMeal(int id, int mealId) {
        return userService.getUserMeal(id, mealId);
    }
}