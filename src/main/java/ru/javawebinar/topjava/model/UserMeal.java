package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class UserMeal {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private long id;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDay() {
        return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), 0, 0);
    }

    public LocalTime getLocalTime() {
        return dateTime.toLocalTime();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
