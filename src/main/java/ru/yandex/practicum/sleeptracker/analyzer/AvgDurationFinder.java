package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AvgDurationFinder implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        double avgDuration = sleepingSessions.stream()
                .map(session -> Duration.between(session.getStart(), session.getFinish()))
                .mapToLong(Duration::toMinutes)
                .average()
                .orElse(0.0);

        String formattedResult = String.format("%.2f", avgDuration);
        return new SleepAnalysisResult("Максимальная продолжительность сна в минутах", formattedResult);
    }
}
