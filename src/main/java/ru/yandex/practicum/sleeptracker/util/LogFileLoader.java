package ru.yandex.practicum.sleeptracker.util;

import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

public class LogFileLoader {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public List<SleepingSession> load(String[] path) throws IOException, IllegalArgumentException, DateTimeParseException {

        if (path.length == 0) {
            throw new IllegalArgumentException("не указан путь к файлу с логом сна");
        }

        File logFile = new File(path[0]);

        try (Stream<String> lines = Files.lines(Paths.get(logFile.getAbsolutePath()), StandardCharsets.UTF_8)) {

            List<SleepingSession> sessions = lines
                    .filter(line -> !line.trim().isEmpty())   // Пропускаем пустые строки
                    .map(line -> {
                            String[] parts = line.split(";");
                            return new SleepingSession(
                                    LocalDateTime.parse(parts[0], DATE_TIME_FORMATTER),
                                    LocalDateTime.parse(parts[1], DATE_TIME_FORMATTER),
                                    SleepQuality.valueOf(parts[2])
                            );
                    })
                    .toList();

            System.out.println("Файл " + logFile.getAbsolutePath() + " загружен успешно");

            if (sessions.isEmpty())
                throw new IOException("сессий не обнаружено, программа закрывается");

            return sessions;

        } catch (FileNotFoundException e) {
            throw new IOException("по пути " + logFile.getAbsolutePath() + " файл не найден", e);
        } catch (DateTimeParseException e) {
            throw new IOException("неверный формат даты в сессии сна", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("недопустимое значение качества сна", e);
        }
    }
}