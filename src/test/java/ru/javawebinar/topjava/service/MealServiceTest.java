package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.jdbc.JdbcMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }


    @Autowired
    private MealService mealService;

    @Autowired
    private JdbcMealRepository jdbcMealRepository;

    @Test
    public void get() {
        Meal expected = mealService.get(ACTUAL_MEAL_ID, TEST_USER_ID);
        assertEquals(expected, getActual());
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.get(ACTUAL_MEAL_ID, BAD_USER_ID));
        assertThrows(NotFoundException.class, () -> mealService.delete(BAD_MEAL_ID, TEST_USER_ID));
    }

    @Test
    public void delete() {
        assertTrue(jdbcMealRepository.delete(ACTUAL_MEAL_ID, TEST_USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> mealService.delete(ACTUAL_MEAL_ID, BAD_USER_ID));
        assertThrows(NotFoundException.class, () -> mealService.delete(BAD_MEAL_ID, TEST_USER_ID));
    }

    @Test
    public void getBetweenInclusiveBadUserId() {
        assertEquals(0, mealService.getBetweenInclusive(START_DATE_TIME.toLocalDate(), END_DATE_TIME.toLocalDate(), BAD_USER_ID).size());
    }

    @Test
    public void getBetweenInclusiveNullDate() {
        List<Meal> a = mealService.getBetweenInclusive(null, null, TEST_USER_ID);
        assertEquals(meals.size(), a.size());

        assertTrue(meals.size() == a.size()
                && a.containsAll(meals)
                && meals.containsAll(a));
    }

    @Test
    public void getBetweenInclusiveDate() {
        LocalDate start = LocalDate.of(2020, Month.JANUARY, 30);
        LocalDate end = LocalDate.of(2020, Month.JANUARY, 31);
        List<Meal> a = mealService.getBetweenInclusive(start, end, TEST_USER_ID);
        List<Meal> b = meals;

        assertTrue(a.size() == b.size()
                && a.containsAll(b)
                && b.containsAll(a));
    }

    @Test
    public void getBetweenInclusiveJdbcRepo() {
        List<Meal> a = jdbcMealRepository.getBetweenHalfOpen(START_DATE_TIME, END_DATE_TIME, TEST_USER_ID);
        List<Meal> b = MealTestData.getBetween();

        assertTrue(a.size() == b.size()
                && a.containsAll(b)
                && b.containsAll(a));
    }

    @Test
    public void getAll() {
        List<Meal> first = mealService.getAll(TEST_USER_ID);
        List<Meal> second = MealTestData.meals;

        assertTrue(first.size() == second.size()
                && first.containsAll(second)
                && second.containsAll(first));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(getUpdated(), TEST_USER_ID);
        Meal expected = mealService.get(ACTUAL_MEAL_ID, TEST_USER_ID);

        assertEquals(expected, updated);
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();

        assertThrows(NotFoundException.class, () -> mealService.update(updated, BAD_USER_ID));
        updated.setId(BAD_MEAL_ID);
        assertThrows(NotFoundException.class, () -> mealService.update(updated, TEST_USER_ID));
    }

    @Test
    public void create() {
        Meal actual = getNew();
        Meal expected = mealService.create(getNew(), TEST_USER_ID);
        Integer createdId = expected.getId();
        actual.setId(createdId);
        assertEquals(expected, actual);
        assertEquals(mealService.get(createdId, TEST_USER_ID), actual);
    }

    @Test
    public void createDuplicateDate() {
        Meal actual = mealService.get(ACTUAL_MEAL_ID, TEST_USER_ID);
        actual.setId(null);
        assertThrows(DataAccessException.class, () -> mealService.create(actual, TEST_USER_ID));
    }
}