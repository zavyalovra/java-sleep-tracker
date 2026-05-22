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

        Period period = Period.between(firstNight, lastNight.plusDays(1));
        return period.getDays() + period.getMonths() * 31;
    }

    private static int getSleepNights(List<SleepingSession> sessions) {
        /*
        * В терминальной операции стрима собираем в коллекцию Set - это автоматически удаляет дубликаты
        * ночей, возникающие в результате наличия нескольких сессий сна за одну ночь.
        * Сессии сна считаются одинаковыми, если у них совпадает только дата начала сессии и ее конца.
        * Реализовано за счет переопределения методов equals/hashCode в классе SleepingSession.
        * */
        return sessions.stream()
                .filter(SleeplessFinder::isSleepConditions)
                .collect(Collectors.toSet()).size();
    }

    private static boolean isSleepConditions(SleepingSession session) {

        LocalDateTime start = session.getStart();
        LocalDateTime finish = session.getFinish();

        if (finish.toLocalDate().isAfter(start.toLocalDate())) return true;
        return start.toLocalTime().isBefore(NIGHT_FINISH);
    }
}
