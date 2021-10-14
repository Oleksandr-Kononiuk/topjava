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
        String forward = "";
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "delete" :
                    long mealId = Long.parseLong(request.getParameter("mealId"));
                    mealDao.delete(mealId);
                    forward = MEALS;
                    request.setAttribute("mealList", mealDao.findAll());
                    log.debug("action=delete, setAttribute(\"mealList\"), forward=MEALS, id=" + mealId);
                    break;
                case "update" :
                    forward = CREATE_UPDATE;
                    long id = Long.parseLong(request.getParameter("mealId"));
                    Meal mealForUpdate = mealDao.findById(id);
                    request.setAttribute("mealForUpdate", mealForUpdate);
                    log.debug("action=update, setAttribute(\"mealForUpdate\"), forward=CREATE_UPDATE, id=" + id);
                    break;
                case "add" :
                    forward = CREATE_UPDATE;
                    log.debug("action=add, forward=CREATE_UPDATE");
                    break;
                default:
                    forward = MEALS;
                    request.setAttribute("mealList", mealDao.findAll());
                    log.debug("action=null, setAttribute(\"mealList\"), forward=MEALS");
            }
        } else {
            forward = MEALS;
            request.setAttribute("mealList", mealDao.findAll());
            log.debug("action=null, setAttribute(\"mealList\"), forward=MEALS");
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals POST");
        req.setCharacterEncoding("UTF8");

        LocalDateTime time = LocalDateTime.parse(req.getParameter("date-time"));
        time.format(dateFormatter);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));

        Meal meal = new Meal(time, description, calories);
        String mealId = req.getParameter("mealId");

        if (mealId == null || mealId.isEmpty()) {
            mealDao.add(meal);
            log.debug("mealId=null or isEmpty(), adding new Meal");
        } else {
            long id = Long.parseLong(mealId);
            mealDao.update(id, meal);
            log.debug("updating new Meal, id=" + id + ", updated Meal=" + mealDao.findById(id).toString());
        }
        RequestDispatcher view = req.getRequestDispatcher(MEALS);
        req.setAttribute("mealList", mealDao.findAll());
        view.forward(req, resp);
    }
}
