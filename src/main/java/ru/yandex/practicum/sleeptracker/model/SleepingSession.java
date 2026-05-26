package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SleepingSession that = (SleepingSession) o;
        return Objects.equals(start, that.start) && Objects.equals(finish, that.finish) && quality == that.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, finish, quality);
    }
}
