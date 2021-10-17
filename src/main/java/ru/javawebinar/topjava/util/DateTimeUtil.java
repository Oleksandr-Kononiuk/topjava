package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(Temporal lt, Temporal startTime, Temporal endTime, Class<? extends Temporal> type) {
        if (type == LocalDateTime.class) {
            return LocalDateTime.from(lt).compareTo(LocalDateTime.from(startTime)) >= 0
                    && LocalDateTime.from(lt).compareTo(LocalDateTime.from(endTime)) < 0;
        } else {
            return LocalTime.from(lt).compareTo(LocalTime.from(startTime)) >= 0
                    && LocalTime.from(lt).compareTo(LocalTime.from(endTime)) < 0;
        }
    }

    public static String toString(LocalDateTime ldt) { //todo
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

