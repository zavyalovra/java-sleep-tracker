package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analyzer.*;
import ru.yandex.practicum.sleeptracker.util.*;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class SleepTrackerApp {
    public static List<SleepingSession> sleepingSessions;
    public static List<Function<List<SleepingSession>, SleepAnalysisResult>> functionList = List.of(
            new TotalSessionsCounter(),
            new MinDurationFinder(),
            new MaxDurationFinder(),
            new AvgDurationFinder(),
            new BadSessionsCounter(),
            new SleeplessFinder(),
            new ChronoTyper()
    );

    public static void main(String[] args) {
        try {
            LogFileLoader logFileLoader = new LogFileLoader();
            sleepingSessions = logFileLoader.load(args);

            System.out.println("\n" + "===== Анализ сессий сна =====");
            functionList.forEach(function ->
                    System.out.println(function.apply(sleepingSessions))
            );

        } catch (IOException e) {
            System.out.println("Ошибка загрузки сессий сна: " + e.getMessage());
        } catch (IllegalArgumentException | DateTimeParseException | NoSuchElementException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}