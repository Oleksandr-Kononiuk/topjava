package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        controller.save(meal);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                String reset = request.getParameter("reset");
                if (reset != null) {
                    log.info("reset filters and getAll");
                    response.sendRedirect("meals");
                    break;
                } else {
                    String startDate = request.getParameter("startDate");
                    String endDate = request.getParameter("endDate");
                    String startTime = request.getParameter("startTime");
                    String endTime = request.getParameter("endTime");

                    if (startDate != null || endDate != null || startTime != null || endTime != null) {
                        log.info("getAll with filter by date from {} to {} and time from {} to {}"
                                , startDate.isEmpty() ? "empty" : startDate, endDate.isEmpty() ? "empty" : endDate
                                , startTime.isEmpty() ? "empty" : startTime, endTime.isEmpty() ? "empty" : endTime);

                        request.setAttribute("meals", controller.getAllWithFilter(
                                startDate == null || startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate),
                                endDate == null || endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate),
                                startTime == null || startTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTime),
                                endTime == null || endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime)
                        ));
                    } else {
                        log.info("getAll");
                        request.setAttribute("meals", controller.getAll());
                    }
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
