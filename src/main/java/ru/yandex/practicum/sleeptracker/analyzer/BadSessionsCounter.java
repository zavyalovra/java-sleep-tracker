package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;

import java.util.List;
import java.util.function.Function;

public class BadSessionsCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        Integer badSessionsCount = sleepingSessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .toList().size();

        return new SleepAnalysisResult("Количество сессий с плохим качеством сна",  badSessionsCount);
    }
}
