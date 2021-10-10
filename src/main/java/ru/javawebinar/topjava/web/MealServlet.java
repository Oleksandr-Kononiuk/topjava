package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to users");
        List<MealTo> mealList = MealsUtil.filteredByStreams(
                MealsUtil.generateMealList(),
                LocalTime.of(7, 0), LocalTime.of(12, 0), MealsUtil.CALORIES_PER_DAY);

//        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        request.setAttribute("mealList", mealList);
        //response.sendRedirect("meals.jsp");
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
