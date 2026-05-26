package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MinDurationFinder implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        Optional<Long> minDuration = sleepingSessions.stream()
                .map(session -> Duration.between(session.getStart(), session.getFinish()))
                .map(Duration::toMinutes)
                .min(Comparator.naturalOrder());

        Long result = minDuration.orElse(0L);
        return new SleepAnalysisResult("Минимальная продолжительность сна в минутах", result);
    }
}
