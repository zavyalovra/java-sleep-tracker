package ru.yandex.practicum.sleeptracker.analyzer;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.UserType;
import ru.yandex.practicum.sleeptracker.util.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.util.SleepingSessionUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronoTyper implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime OWL_SLEEP_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_SLEEP_FINISH = LocalTime.of(9, 0);
    private static final LocalTime LARK_SLEEP_START = LocalTime.of(22, 0);
    private static final LocalTime LARK_SLEEP_FINISH = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        if (sleepingSessions == null || sleepingSessions.isEmpty())
            throw new NoSuchElementException("список сессий сна пуст");

        Map<UserType, Long> typeCounts = sleepingSessions.stream()
                .filter(SleepingSessionUtils::isNightSession)
                .filter(SleepingSessionUtils::isValidSession)
                .map(ChronoTyper::getUserType)
                .collect(Collectors.groupingBy(
                        type -> type,
                        Collectors.counting()
                ));

        if (typeCounts.isEmpty())
            throw new NoSuchElementException("в списке сессий нет ночей со сном");

        Long maxCountChronoType = typeCounts.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        List<UserType> topListOfChronoType = typeCounts.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxCountChronoType))
                .map(Map.Entry::getKey)
                .toList();

        UserType result = topListOfChronoType.size() > 1 ? UserType.DOVE : topListOfChronoType.getFirst();
        return new SleepAnalysisResult("Классификация пользователя по хронотипу", result.getName());
    }

    private static UserType getUserType(SleepingSession sleepingSession) {
        LocalTime sleepStart = sleepingSession.getStart().toLocalTime();
        LocalTime sleepFinish = sleepingSession.getFinish().toLocalTime();

        boolean isCrossMidnight = sleepFinish.isBefore(sleepStart);

        if (isCrossMidnight) {
            if (sleepStart.isAfter(OWL_SLEEP_START) && sleepFinish.isAfter(OWL_SLEEP_FINISH)) {
                return UserType.OWL;
            } else if (sleepStart.isBefore(LARK_SLEEP_START) && sleepFinish.isBefore(LARK_SLEEP_FINISH)) {
                return UserType.LARK;
            }
        } else {
            if (sleepFinish.isAfter(OWL_SLEEP_FINISH)) {
                return UserType.OWL;
            }
        }

        return UserType.DOVE;
    }
}
