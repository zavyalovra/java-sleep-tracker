package ru.yandex.practicum.sleeptracker.util;

public class SleepAnalysisResult {
    private final String description;
    private final Object result;

    public SleepAnalysisResult(String description, Object result) {
        this.description = description;
        this.result = result;
    }

    @Override
    public String toString() {
        return description + ": " + result;
    }
}
