package ru.yandex.practicum.sleeptracker.util;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SleepingSessionUtils {
    private static final LocalTime NIGHT_FINISH = LocalTime.of(6, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);
    private static final int SLEEP_MAX_HOURS = 20;

    public static LocalDate getNightDate(SleepingSession session) {

        LocalDateTime start = session.getStart();

        return start.toLocalTime().isAfter(NOON)
                ? start.toLocalDate().plusDays(1)
                : start.toLocalDate();
    }

    public static boolean isNightSession(SleepingSession sleepingSession) {
        LocalDateTime sleepStart = sleepingSession.getStart();
        LocalDateTime sleepFinish = sleepingSession.getFinish();

        LocalDate nightDate = sleepStart.toLocalTime().isAfter(LocalTime.NOON)
                ? sleepStart.toLocalDate().plusDays(1)
                : sleepStart.toLocalDate();

        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightFinish = nightDate.atTime(NIGHT_FINISH);

        return sleepStart.isBefore(nightFinish) && sleepFinish.isAfter(nightStart);
    }

    public static boolean isSleepConditions(SleepingSession session) {

        LocalDate nightDate = getNightDate(session);

        LocalDateTime start = nightDate.atStartOfDay();
        LocalDateTime finish = nightDate.atTime(NIGHT_FINISH);

        return session.getStart().isBefore(finish) && session.getFinish().isAfter(start);
    }

    public static boolean isValidSession(SleepingSession session) {
        return Duration.between(session.getStart(), session.getFinish()).toHours() <= SLEEP_MAX_HOURS;
    }
}
