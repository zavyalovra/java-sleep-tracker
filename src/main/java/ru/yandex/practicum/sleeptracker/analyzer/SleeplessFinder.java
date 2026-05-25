package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessFinder implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime NIGHT_FINISH = LocalTime.of(6, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        int nightWithoutSleeping = getTotalNights(sleepingSessions) - getSleepNights(sleepingSessions);

        return new SleepAnalysisResult("Выявлено бессонных ночей",  nightWithoutSleeping);
    }

    private static int getTotalNights(List<SleepingSession> sessions) {

        if (sessions == null || sessions.isEmpty()) return 0;

        LocalDate firstNight = getNightDate(sessions.getFirst());
        LocalDate lastNight = getNightDate(sessions.getLast());

        long period = lastNight.plusDays(1).toEpochDay() - firstNight.toEpochDay();
        return (int) period;
    }

    private static int getSleepNights(List<SleepingSession> sessions) {

        return sessions.stream()
                .filter(SleeplessFinder::isSleepConditions)
                .map(SleeplessFinder::getNightDate)
                .collect(Collectors.toSet())
                .size();
    }

    private static boolean isSleepConditions(SleepingSession session) {

        LocalDate nightDate = getNightDate(session);

        LocalDateTime start = nightDate.atStartOfDay();
        LocalDateTime finish = nightDate.atTime(NIGHT_FINISH);

        return session.getStart().isBefore(finish) && session.getFinish().isAfter(start);
    }

    private static LocalDate getNightDate(SleepingSession session) {

        LocalDateTime start = session.getStart();

        return start.toLocalTime().isAfter(NOON)
                ? start.toLocalDate().plusDays(1)
                : start.toLocalDate();
    }
}
