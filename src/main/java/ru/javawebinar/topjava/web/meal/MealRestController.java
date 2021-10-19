package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.to.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
        {
            MealsUtil.meals.forEach(this::save);
        }
    }

    public Meal save(Meal meal) {
        log.info("save: user id {}, {}", authUserId(), meal.toString());
        return service.create(authUserId(), meal);
    }

    public void update(Meal meal) {
        log.info("update: user id {}, {}", authUserId(), meal.toString());
        service.update(authUserId(), meal);
    }

    public void delete(int id) {
        log.info("delete: user id {}, meal id {}", authUserId(), id);
        service.delete(authUserId(), id);
    }

    public Meal get(int id) {
        log.info("get: user id {}, meal id {}", authUserId(), id);
        return service.get(authUserId(), id);
    }

    public List<MealTo> getAll() {
        log.info("getAll: user id {}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllWithFilter(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll with filter by date from {} to {} and time from {} to {} for user: {}"
                , startDate, endDate, startTime, endTime, authUserId());

        return MealsUtil.getFilteredTos(
                service.getAllFilteredByDate(authUserId(), startDate, endDate)
                , authUserCaloriesPerDay(), startTime, endTime
        );
    }
}