package ru.yandex.practicum.sleeptracker.model;

public enum UserType {
    OWL("сова"),
    LARK("жаворонок"),
    DOVE("голубь");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
