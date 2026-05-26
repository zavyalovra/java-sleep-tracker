package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.util.SleepingSessionUtils;
import static ru.yandex.practicum.sleeptracker.util.SleepingSessionUtils.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessFinder implements Function<List<SleepingSession>, SleepAnalysisResult> {

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
                .filter(SleepingSessionUtils::isSleepConditions)
                .map(SleepingSessionUtils::getNightDate)
                .collect(Collectors.toSet())
                .size();
    }
}
