package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime start;
    private final LocalDateTime finish;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime finish, SleepQuality quality) {
        this.start = start;
        this.finish = finish;
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "start=" + start +
                ", finish=" + finish +
                ", quality=" + quality +
                '}';
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public SleepQuality getQuality() {
        return quality;
    }
}
