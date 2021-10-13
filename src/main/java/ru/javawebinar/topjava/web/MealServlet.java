package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.impl.MealDaoImpl;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private MealDao mealDao;
    private static final long serialVersionUID = 1L;
    private static final String CREATE_UPDATE = "/create-update-meal.jsp";
    private static final String MEALS = "/meals.jsp";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm");

    public MealServlet() {
        super();
        mealDao = new MealDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals GET");
        String forForward = "";
        String action = request.getParameter("action");

        //todo switch...case
        if (action == null) {
            forForward = MEALS;
            request.setAttribute("mealList", mealDao.findAll());
            log.debug("action == null, setAttribute(\"mealList\"), forForward = MEALS");

        } else if (action.equalsIgnoreCase("delete")) {
            long mealId = Long.parseLong(request.getParameter("mealId"));
            mealDao.delete(mealId);
            forForward = MEALS;
            request.setAttribute("mealList", mealDao.findAll());
            log.debug("action == delete, setAttribute(\"mealList\"), forForward = MEALS mealId = " + mealId);

        } else if (action.equalsIgnoreCase("update")) {
            forForward = CREATE_UPDATE;
            long mealId = Long.parseLong(request.getParameter("mealId"));
            Meal mealForUpdate = mealDao.findById(mealId);
            request.setAttribute("mealForUpdate", mealForUpdate);
            log.debug("action == update, setAttribute(\"mealForUpdate\"), forForward = CREATE_UPDATE, mealId = " + mealId);

        } else if (action.equalsIgnoreCase("add")) {
            forForward = CREATE_UPDATE;
            log.debug("action == add, forForward = CREATE_UPDATE");

        } else {
            forForward = MEALS;
            log.debug("forForward = MEALS");
        }
        RequestDispatcher view = request.getRequestDispatcher(forForward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //2021-10-12T18:17
        log.debug("redirect to meals POST");
        req.setCharacterEncoding("UTF8");

        LocalDateTime time = LocalDateTime.parse(req.getParameter("date-time"));
        //time.format(dateFormatter);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        Meal meal = new Meal(time, description, calories);

        String mealId = req.getParameter("mealId");
        System.out.println("doPost id: " + mealId);

        if (mealId == null || mealId.isEmpty()) {
            mealDao.add(meal);
            log.debug("mealId == null || isEmpty(), adding new Meal");
        } else {
            long id = Long.parseLong(mealId);
            mealDao.update(id, meal);
            log.debug("updating new Meal, mealId = " + id + " updated Meal = " + meal.toString());
        }
        RequestDispatcher view = req.getRequestDispatcher(MEALS);
        req.setAttribute("mealList", mealDao.findAll());
        view.forward(req, resp);
    }
}
