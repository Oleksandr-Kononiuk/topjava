package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final List<Meal> meals = Arrays.asList(
            new Meal(100002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(100005, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(100006, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(100007, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(100008, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    public static final int BAD_USER_ID = 999999;
    public static final int TEST_USER_ID = 100000;
    public static final int ACTUAL_MEAL_ID = 100002;
    public static final int BAD_MEAL_ID = -1;
    public static final LocalDateTime START_DATE_TIME = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0).truncatedTo(ChronoUnit.HOURS);
    public static final LocalDateTime END_DATE_TIME = LocalDateTime.of(2020, Month.JANUARY, 30, 21, 0).truncatedTo(ChronoUnit.HOURS);


    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "Завтрак", 500);
    }

    public static Meal getActual() {
        return new Meal(100002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    }

    public static Meal getUpdated() {
        return new Meal(100002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Updated", 500);
    }

    public static List<Meal> getBetween() {
        return meals.subList(0, 3);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("registered", "roles").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
