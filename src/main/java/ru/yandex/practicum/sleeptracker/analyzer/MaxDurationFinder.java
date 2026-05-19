package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MaxDurationFinder implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        Optional<Long> maxDuration = sleepingSessions.stream()
                .map(session -> Duration.between(session.getStart(), session.getFinish()))
                .map(Duration::toMinutes)
                .max(Comparator.naturalOrder());

        Long result = maxDuration.orElse(null);
        return new SleepAnalysisResult("Максимальная продолжительность сна в минутах", result);
    }
}
