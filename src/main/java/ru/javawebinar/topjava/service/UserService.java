package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserService {

    private final UserRepository userRepository;
    private MealService mealService;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    public User get(int id) {
        return checkNotFoundWithId(userRepository.get(id), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(userRepository.save(user), user.id());
    }

    @Transactional(readOnly = true)
    public Map<User, List<Meal>> getUserMeals(int id) {
        Map<User, List<Meal>> m = new HashMap<>();
        m.put(get(id), mealService.getAll(id));
        return m;
    }

    @Transactional(readOnly = true)
    public Map<User, Meal> getUserMeal(int id, int mealId) {
        Map<User, Meal> m = new HashMap<>();
        m.put(get(id), mealService.get(mealId, id));
        return m;
    }
}