package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analyzer.*;
import ru.yandex.practicum.sleeptracker.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    public static List<String> inputData;
    public static List<SleepingSession> sessions;

    @BeforeAll
    static void initData() {
        inputData = List.of(
                "01.10.25 23:15;02.10.25 07:30;GOOD",
                "02.10.25 23:50;03.10.25 06:40;NORMAL",
                "03.10.25 14:10;03.10.25 15:00;NORMAL",
                "03.10.25 23:40;04.10.25 08:00;BAD",
                "05.10.25 00:10;05.10.25 06:20;GOOD",
                "05.10.25 13:30;05.10.25 14:15;NORMAL",
                "06.10.25 22:30;07.10.25 05:50;GOOD",
                "07.10.25 23:45;08.10.25 06:30;GOOD",
                "08.10.25 23:50;09.10.25 07:10;GOOD",
                "10.10.25 13:00;10.10.25 14:30;NORMAL",
                "10.10.25 23:55;11.10.25 06:10;GOOD",
                "11.10.25 23:10;12.10.25 07:00;BAD",
                "30.10.25 23:50;31.10.25 06:30;GOOD"
        );

        sessions = inputData.stream()
                .map(line -> {
                    String[] parts = line.split(";");
                    return new SleepingSession(
                            LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            SleepQuality.valueOf(parts[2])
                    );
                })
                .toList();
    }

    @Test
    void shouldReturnTotalSessionsCount() {
        TotalSessionsCounter totalSessionsCounter = new TotalSessionsCounter();
        assertEquals(13, totalSessionsCounter.apply(sessions).getResult());
    }

    @Test
    void shouldReturnZeroSessionsCount() {
        TotalSessionsCounter totalSessionsCounter = new TotalSessionsCounter();
        List<SleepingSession> modifiableSessions = new ArrayList<>(sessions);
        modifiableSessions.clear();
        assertEquals(0, totalSessionsCounter.apply(modifiableSessions).getResult());
    }

    @Test
    void shouldReturnMinSessionsDuration() {
        MinDurationFinder minDurationFinder = new MinDurationFinder();
        assertEquals(45L, minDurationFinder.apply(sessions).getResult());
    }

    @Test
    void shouldReturnZeroMinSessionsDuration() {
        MinDurationFinder minDurationFinder = new MinDurationFinder();
        List<SleepingSession> modifiableSessions = new ArrayList<>(sessions);
        modifiableSessions.clear();
        assertEquals(0L, minDurationFinder.apply(modifiableSessions).getResult());
    }

    @Test
    void shouldReturnMaxSessionsDuration() {
        MaxDurationFinder maxDurationFinder = new MaxDurationFinder();
        assertEquals(500L, maxDurationFinder.apply(sessions).getResult());
    }

    @Test
    void shouldReturnZeroMaxSessionsDuration() {
        MaxDurationFinder maxDurationFinder = new MaxDurationFinder();
        List<SleepingSession> modifiableSessions = new ArrayList<>(sessions);
        modifiableSessions.clear();
        assertEquals(0L, maxDurationFinder.apply(modifiableSessions).getResult());
    }

    @Test
    void shouldReturnAvgSessionsDuration() {
        AvgDurationFinder avgDurationFinder = new AvgDurationFinder();
        assertEquals("345.38", avgDurationFinder.apply(sessions).getResult());
    }

    @Test
    void shouldReturnZeroAvgSessionsDuration() {
        AvgDurationFinder avgDurationFinder = new AvgDurationFinder();
        List<SleepingSession> modifiableSessions = new ArrayList<>(sessions);
        modifiableSessions.clear();
        assertEquals("0.00", avgDurationFinder.apply(modifiableSessions).getResult());
    }

    @Test
    void shouldReturnBadSessionsCount() {
        BadSessionsCounter badSessionsCounter = new BadSessionsCounter();
        assertEquals(2, badSessionsCounter.apply(sessions).getResult());
    }

    @Test
    void shouldReturnZeroBadSessionsCount() {
        BadSessionsCounter badSessionsCounter = new BadSessionsCounter();

        List<SleepingSession> modifiableSessions = sessions.stream()
                                .filter(session -> !session.getQuality().equals(SleepQuality.BAD))
                                .toList();

        assertEquals(0, badSessionsCounter.apply(modifiableSessions).getResult());
    }
    /*
    * Бессонные ночи
    * Проверка стандартного лога задания
    * */

    @Test
    void shouldBeReturn20() {
        SleeplessFinder sleeplessFinder  = new SleeplessFinder();
        assertEquals(20, sleeplessFinder.apply(sessions).getResult());
    }
    /*
     * Бессонные ночи
     * Убираем ночи без описания (часть бессонных ночей)
     * за счет сокращения интервала логирования до 12.10.25
     * */

    @Test
    void shouldBeReturn2() {
        SleeplessFinder sleeplessFinder  = new SleeplessFinder();

        List<SleepingSession> modifiableSessions = sessions.stream()
                .limit(sessions.size() - 1)
                .toList();

        assertEquals(2, sleeplessFinder.apply(modifiableSessions).getResult());
    }
    /*
     * Бессонные ночи
     * Проверяем функцию на работу с пустым списком
     * */

    @Test
    void shouldBeReturn0() {
        SleeplessFinder sleeplessFinder  = new SleeplessFinder();
        List<SleepingSession> modifiableSessions = new ArrayList<>(sessions);
        modifiableSessions.clear();

        assertEquals(0, sleeplessFinder.apply(modifiableSessions).getResult());
    }
    /*
     * Бессонные ночи
     * Проверяем функцию на учет ночей,
     * когда несколько сессий сна в пределах одной ночи
     * */

    @Test
    void shouldBeReturn16() {
        SleeplessFinder sleeplessFinder  = new SleeplessFinder();

        inputData = List.of(
                "01.10.25 23:15;02.10.25 07:30;GOOD",
                "02.10.25 23:50;03.10.25 06:40;NORMAL",
                "03.10.25 14:10;03.10.25 15:00;NORMAL",
                "03.10.25 23:40;04.10.25 08:00;BAD",
                "05.10.25 00:10;05.10.25 03:20;NORMAL",
                "05.10.25 03:35;05.10.25 06:20;NORMAL",
                "05.10.25 13:30;05.10.25 14:15;NORMAL",
                "06.10.25 22:30;07.10.25 05:50;GOOD",
                "07.10.25 23:45;08.10.25 06:30;GOOD",
                "08.10.25 23:50;09.10.25 07:10;GOOD",
                "10.10.25 13:00;10.10.25 14:30;NORMAL",
                "10.10.25 23:55;11.10.25 06:10;GOOD",
                "11.10.25 23:10;12.10.25 07:00;BAD",
                "12.10.25 21:45;13.10.25 05:45;NORMAL",
                "13.10.25 21:45;14.10.25 05:45;NORMAL",
                "14.10.25 00:45;14.10.25 04:00;NORMAL",
                "14.10.25 04:05;15.10.25 10:45;GOOD",
                "30.10.25 23:50;31.10.25 06:30;GOOD"
        );

        List<SleepingSession> modifiedSessions = inputData.stream()
                .map(line -> {
                    String[] parts = line.split(";");
                    return new SleepingSession(
                            LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            SleepQuality.valueOf(parts[2])
                    );
                })
                .toList();

        assertEquals(16, sleeplessFinder.apply(modifiedSessions).getResult());
    }
    /*
     * Бессонные ночи
     * Проверяем функцию на работу с переходом даты через
     * границу между двух месяцев
     * */

    @Test
    void shouldBeReturn1() {
        SleeplessFinder sleeplessFinder  = new SleeplessFinder();

        inputData = List.of(
                "29.09.25 22:50;30.09.25 08:30;GOOD",
                "30.09.25 23:50;01.10.25 09:30;GOOD",
                "01.10.25 23:15;02.10.25 07:30;GOOD",
                "02.10.25 23:50;03.10.25 06:40;NORMAL",
                "03.10.25 14:10;03.10.25 15:00;NORMAL",
                "03.10.25 23:40;04.10.25 08:00;BAD",
                "05.10.25 00:10;05.10.25 03:20;NORMAL",
                "05.10.25 03:35;05.10.25 06:20;NORMAL",
                "05.10.25 13:30;05.10.25 14:15;NORMAL",
                "06.10.25 22:30;07.10.25 05:50;GOOD",
                "07.10.25 23:45;08.10.25 06:30;GOOD",
                "08.10.25 23:50;09.10.25 07:10;GOOD"
        );

        List<SleepingSession> modifiedSessions = inputData.stream()
                .map(line -> {
                    String[] parts = line.split(";");
                    return new SleepingSession(
                            LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            SleepQuality.valueOf(parts[2])
                    );
                })
                .toList();

        assertEquals(1, sleeplessFinder.apply(modifiedSessions).getResult());
    }
    /*
    * Алгоритм классификации пользователя
    * Работа с логом задания, должен вернуть Dove
    * */

    @Test
    void shouldBeReturnCorrectUserType() {
        ChronoTyper chronoTyper  = new ChronoTyper();
        assertEquals(UserType.DOVE.getName(), chronoTyper.apply(sessions).getResult());
    }
    /*
     * Алгоритм классификации пользователя
     * Модифицированный лог для результата: OWL
     * */

    @Test
    void shouldBeReturnOWL() {
        ChronoTyper chronoTyper  = new ChronoTyper();

        inputData = List.of(
                "29.09.25 23:50;30.09.25 09:30;GOOD",
                "30.09.25 00:50;01.10.25 09:30;GOOD",
                "01.10.25 23:15;02.10.25 10:30;GOOD",
                "02.10.25 23:50;03.10.25 10:40;NORMAL",
                "03.10.25 14:10;03.10.25 15:00;NORMAL",
                "03.10.25 23:40;04.10.25 10:00;BAD",
                "05.10.25 00:10;05.10.25 03:20;NORMAL",
                "05.10.25 03:35;05.10.25 10:20;NORMAL",
                "05.10.25 13:30;05.10.25 14:15;NORMAL",
                "06.10.25 21:30;07.10.25 05:50;GOOD",
                "07.10.25 21:45;08.10.25 05:30;GOOD",
                "08.10.25 21:50;09.10.25 05:55;GOOD"
        );

        List<SleepingSession> modifiedSessions = inputData.stream()
                .map(line -> {
                    String[] parts = line.split(";");
                    return new SleepingSession(
                            LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            SleepQuality.valueOf(parts[2])
                    );
                })
                .toList();

        assertEquals(UserType.OWL.getName(), chronoTyper.apply(modifiedSessions).getResult());
    }
    /*
     * Алгоритм классификации пользователя
     * Модифицированный лог для результата: LARK
     * */

    @Test
    void shouldBeReturnLARK() {
        ChronoTyper chronoTyper  = new ChronoTyper();

        inputData = List.of(
                "29.09.25 21:50;30.09.25 05:30;GOOD",
                "30.09.25 21:50;01.10.25 05:30;GOOD",
                "01.10.25 21:15;02.10.25 05:30;GOOD",
                "02.10.25 21:50;03.10.25 05:40;NORMAL",
                "03.10.25 14:10;03.10.25 15:00;NORMAL",
                "03.10.25 23:40;04.10.25 10:00;BAD",
                "05.10.25 00:10;05.10.25 03:20;NORMAL",
                "05.10.25 03:35;05.10.25 10:20;NORMAL",
                "05.10.25 13:30;05.10.25 14:15;NORMAL",
                "06.10.25 21:30;07.10.25 05:50;GOOD",
                "07.10.25 21:45;08.10.25 05:30;GOOD",
                "08.10.25 21:50;09.10.25 05:55;GOOD"
        );

        List<SleepingSession> modifiedSessions = inputData.stream()
                .map(line -> {
                    String[] parts = line.split(";");
                    return new SleepingSession(
                            LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            SleepQuality.valueOf(parts[2])
                    );
                })
                .toList();

        assertEquals(UserType.LARK.getName(), chronoTyper.apply(modifiedSessions).getResult());
    }
    /*
     * Алгоритм классификации пользователя
     * Модифицированный лог для результата:
     * исходные данные предусматривают одинаковое количество сессий типа OWL и LARK,
     * в этом случае метод должен возвращать Dove
     * */

    @Test
    void shouldBeReturnDOVE() {
        ChronoTyper chronoTyper  = new ChronoTyper();

        inputData = List.of(
                "29.09.25 21:50;30.09.25 05:30;GOOD",
                "30.09.25 21:50;01.10.25 05:30;GOOD",
                "01.10.25 21:15;02.10.25 05:30;GOOD",
                "02.10.25 21:50;03.10.25 05:40;NORMAL",
                "03.10.25 14:10;03.10.25 15:00;NORMAL",
                "03.10.25 23:40;04.10.25 10:00;BAD",
                "05.10.25 00:10;05.10.25 03:20;NORMAL",
                "05.10.25 03:35;05.10.25 10:20;NORMAL",
                "05.10.25 13:30;05.10.25 14:15;NORMAL",
                "06.10.25 23:30;07.10.25 10:50;GOOD",
                "07.10.25 23:45;08.10.25 10:30;GOOD"
        );

        List<SleepingSession> modifiedSessions = inputData.stream()
                .map(line -> {
                    String[] parts = line.split(";");
                    return new SleepingSession(
                            LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            LocalDateTime.parse(parts[1], DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")),
                            SleepQuality.valueOf(parts[2])
                    );
                })
                .toList();

        assertEquals(UserType.DOVE.getName(), chronoTyper.apply(modifiedSessions).getResult());
    }
}