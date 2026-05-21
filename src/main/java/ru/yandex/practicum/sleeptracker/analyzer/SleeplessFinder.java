package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessFinder implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime NIGHT_FINISH = LocalTime.of(6, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);
    private static final LocalTime MIDNIGHT = LocalTime.of(0, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        int nightWithoutSleeping = getTotalNights(sleepingSessions) - getSleepNights(sleepingSessions);

        return new SleepAnalysisResult("Выявлено бессонных ночей",  nightWithoutSleeping);
    }

    private static int getTotalNights(List<SleepingSession> sessions) {

        if (sessions == null || sessions.isEmpty()) return 0;

        LocalDate firstNight = sessions.getFirst().getStart().toLocalTime().isAfter(NOON)
                ? sessions.getFirst().getStart().toLocalDate().plusDays(1)
                : sessions.getFirst().getStart().toLocalDate();

        LocalDate lastNight = sessions.getLast().getFinish().toLocalDate();

        if (lastNight.isBefore(firstNight)) {
            return 0;
        }

        return Period.between(firstNight, lastNight.plusDays(1)).getDays();
    }

    private static int getSleepNights(List<SleepingSession> sessions) {

        return sessions.stream()
                .filter(SleeplessFinder::isSleepConditions)
                .collect(Collectors.toSet()).size();
    }

    private static boolean isSleepConditions(SleepingSession session) {

        LocalDateTime start = session.getStart();
        LocalDateTime finish = session.getFinish();

        LocalTime startTime = start.toLocalTime();
        LocalTime finishTime = finish.toLocalTime();

        if (finish.toLocalDate().isAfter(start.toLocalDate())) return true;
        if (startTime.isAfter(MIDNIGHT) && finishTime.isBefore(NIGHT_FINISH)) return true;
        if (startTime.isBefore(NIGHT_FINISH)) return true;

        return false;
    }
}
