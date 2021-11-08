package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private MealService mealService;

    @Autowired
    private UserService userService;

    private static final StringBuilder results = new StringBuilder();
    private static final Logger log = getLogger("result");

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Before
    public void setup() {
        if (this.getClass().getSimpleName().equals("MealServiceTest")) {
            cacheManager.getCache("meals").clear();
        }
        if (this.getClass().getSimpleName().equals("UserServiceTest")) {
            cacheManager.getCache("users").clear();
        }
    }

    @Test
    public void getUserMeals() {
        Map<User, List<Meal>> m = new HashMap<>();
        if (this.getClass().getSimpleName().equals("UserServiceTest")) {
            m = userService.getUserMeals(UserTestData.ADMIN_ID);
        }
        if (this.getClass().getSimpleName().equals("MealServiceTest")) {
            m = mealService.getUserMeals(UserTestData.ADMIN_ID);
        }
        List<Meal> actualAdminMeals = MealTestData.getAdminMeals();
        List<Meal> expectedAdminMeals = m.get(UserTestData.admin);

        UserTestData.USER_MATCHER.assertMatch(m.keySet().iterator().next(), UserTestData.admin);
        MealTestData.MEAL_MATCHER.assertMatch(expectedAdminMeals, actualAdminMeals);
    }

    @Test
    public void getUserMeal() {
        Map<User, Meal> m = new HashMap<>();
        if (this.getClass().getSimpleName().equals("UserServiceTest")) {
            m = userService.getUserMeal(UserTestData.USER_ID, MealTestData.MEAL1_ID);
        }
        if (this.getClass().getSimpleName().equals("MealServiceTest")) {
            m = mealService.getUserMeal(UserTestData.USER_ID, MealTestData.MEAL1_ID);
        }
        Meal expectedMeal = m.get(UserTestData.user);

        UserTestData.USER_MATCHER.assertMatch(m.keySet().iterator().next(), UserTestData.user);
        MealTestData.MEAL_MATCHER.assertMatch(expectedMeal, MealTestData.meal1);
    }
}
